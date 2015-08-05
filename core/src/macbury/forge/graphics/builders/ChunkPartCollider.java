package macbury.forge.graphics.builders;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlockShape;
import macbury.forge.blocks.BlockShapePart;
import macbury.forge.blocks.BlockShapeTriangle;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.systems.PhysicsSystem;
import macbury.forge.terrain.greedy.AbstractGreedyAlgorithm;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 27.04.15.
 */
public class ChunkPartCollider implements Disposable {
  private static final String TAG = "ChunkPartCollider";
  private static final float CHUNK_FRICTION = 0.85f;
  public final static Matrix4 transformMat = new Matrix4();
  public final static Vector3 tempSize = new Vector3();
  public final static Vector3 tempPos = new Vector3();
  public final Vector3i position = new Vector3i();
  public final Vector3i size     = new Vector3i();
  public final Block.Side side;
  public Block block;
  public Voxel voxel;
  private BlockShape blockShape;
  private btConvexHullShape shape;
  private btRigidBody body;
  private btDiscreteDynamicsWorld bulletWorld;

  public ChunkPartCollider(AbstractGreedyAlgorithm.GreedyQuad quad, Block.Side side) {
    size.set(quad.voxelSize);
    position.set(quad.voxelPosition);
    this.blockShape = quad.block.blockShape;
    this.voxel      = quad.voxel;
    this.block      = quad.block;
    this.side       = side;
  }

  public ChunkPartCollider(Voxel voxel, Block.Side side) {
    this.side       = side;
    this.block      = voxel.getBlock();
    this.blockShape = block.blockShape;
    this.voxel      = voxel;
  }

  @Override
  public void dispose() {
    if (body != null) {
      if (bulletWorld != null)
        bulletWorld.removeRigidBody(body);
      body.dispose();
      shape.dispose();
    }
    position.setZero();
    size.setZero();
    blockShape = null;
    voxel = null;
    shape = null;
    body  = null;
    block = null;
  }


  public void initializeAndAddToWorld(btDiscreteDynamicsWorld bulletWorld) {
    //Gdx.app.log(TAG, "adding collider to bullet world at " + position.toString() + " size: " + size.toString());
    bulletWorld.addRigidBody(body, PhysicsSystem.Flags.Ground.mask, PhysicsSystem.Flags.All.mask);
    this.bulletWorld = bulletWorld;
  }

  public boolean canAssemble() {
    return blockShape.get(side) != null;
  }


  private void helpAddPointToShape(Vector3 voxelSize, Vector3 point,  btConvexHullShape convexHullShape) {
    VoxelsAssembler.vertexTranslationFromShape(
        Vector3i.ZERO,
        size,
        tempPos.set(voxelSize).scl(0.5f),
        voxel.alginTo,
        block.rotation,
        point,
        tempPos
    );
    convexHullShape.addPoint(tempPos, false);
  }

  public void assemble(Vector3 voxelSize) {
    BlockShapePart part = blockShape.get(side);

    btConvexHullShape convexHullShape = new btConvexHullShape();
    for (BlockShapeTriangle triangle : part.triangles) {
      helpAddPointToShape(voxelSize, part.verticies.get(triangle.index1), convexHullShape);
      helpAddPointToShape(voxelSize, part.verticies.get(triangle.index2), convexHullShape);
      helpAddPointToShape(voxelSize, part.verticies.get(triangle.index3), convexHullShape);

    }

    convexHullShape.recalcLocalAabb();

    position.applyTo(tempPos);
    transformMat.idt().translate(tempPos);

    shape = convexHullShape;

    btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, shape, Vector3.Zero);
    constructionInfo.setFriction(CHUNK_FRICTION);
    this.body  = new btRigidBody(constructionInfo);
    body.setWorldTransform(transformMat);
    body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
    constructionInfo.dispose();
  }

  @Override
  public boolean equals(Object obj) {
    ChunkPartCollider other = (ChunkPartCollider)obj;
    return other.position.equals(position) && other.size.equals(other.size) && other.blockShape == this.blockShape;
  }

}
