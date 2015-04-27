package macbury.forge.graphics.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import macbury.forge.blocks.Block;
import macbury.forge.blocks.BlockShape;
import macbury.forge.blocks.BlockShapePart;
import macbury.forge.graphics.mesh.VoxelsAssembler;
import macbury.forge.terrain.greedy.AbstractGreedyAlgorithm;
import macbury.forge.utils.Vector3i;
import macbury.forge.voxel.Voxel;

/**
 * Created by macbury on 27.04.15.
 */
public class ChunkPartCollider implements Disposable {
  private static final String TAG = "ChunkPartCollider";
  public final Vector3i position = new Vector3i();
  public final Vector3i size     = new Vector3i();
  public final static Matrix4 transformMat = new Matrix4();
  public final static Vector3 tempSize = new Vector3();
  public final static Vector3 tempPos = new Vector3();
  private Block block;
  private Voxel voxel;
  private BlockShape blockShape;
  private btConvexHullShape shape;
  private btRigidBody body;
  private btDiscreteDynamicsWorld bulletWorld;

  public ChunkPartCollider(AbstractGreedyAlgorithm.GreedyQuad quad) {
    size.set(quad.voxelSize);
    position.set(quad.voxelPosition);
    this.blockShape = quad.block.blockShape;
    this.voxel      = quad.voxel;
    this.block      = quad.block;
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
    bulletWorld.addRigidBody(body);
    this.bulletWorld = bulletWorld;
  }

  public boolean canAssemble(Block.Side side) {
    return blockShape.get(side) != null;
  }

  public void assemble(Vector3 voxelSize, Block.Side side) {
    BlockShapePart part = blockShape.get(side);

    shape = new btConvexHullShape();
    for (Vector3 point : part.verticies) {
      VoxelsAssembler.vertexTranslationFromShape(
          Vector3i.ZERO,
          size,
          tempPos.set(voxelSize).scl(0.5f),
          side,
          block.rotation,
          point,
          tempPos
      );
      shape.addPoint(tempPos, false);
    }
    shape.recalcLocalAabb();

    position.applyTo(tempPos);
    transformMat.idt().translate(tempPos);

    btRigidBody.btRigidBodyConstructionInfo constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, shape, Vector3.Zero);
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
