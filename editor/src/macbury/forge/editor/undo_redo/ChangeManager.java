package macbury.forge.editor.undo_redo;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import macbury.forge.editor.parell.JobManager;
import macbury.forge.editor.parell.jobs.changeable.ApplyChangableJob;
import macbury.forge.editor.parell.jobs.changeable.RevertChangeableJob;

/**
 * Created by macbury on 31.10.14.
 */

public class ChangeManager implements Disposable {
  private JobManager jobsManager;
  private Node currentIndex = null;
  private Node parentNode = new Node();
  private Array<ChangeManagerListener> listeners;
  /**
   * Creates a new ChangeManager object which is initially empty.
   * @param jobs
   */
  public ChangeManager(JobManager jobs){
    currentIndex = parentNode;
    this.listeners = new Array<ChangeManagerListener>();
    this.jobsManager = jobs;
  }

  public void addListener(ChangeManagerListener listener) {
    if (listeners.indexOf(listener, true) == -1) {
      listeners.add(listener);
    }
  }


  public void removeListener(ChangeManagerListener listener) {
    if (listeners.indexOf(listener, true) != -1) {
      listeners.removeValue(listener, true);
    }
  }

  /**
   * Clears all Changables contained in this manager.
   */
  public void clear(){
    parentNode   = new Node();
    currentIndex = parentNode;
    for (ChangeManagerListener listener : listeners) {
      listener.onChangeManagerChange(this);
    }
  }


  /**
   * Adds a Changeable to manage.
   * @param changeable
   */
  private Changeable addChangeable(Changeable changeable){
    Node node          = new Node(changeable);
    currentIndex.right = node;
    node.left          = currentIndex;
    currentIndex       = node;
    for (ChangeManagerListener listener : listeners) {
      listener.onChangeManagerChange(this);
    }

    return changeable;
  }

  public void pushAndExecute(Changeable changeable) {
    addChangeable(changeable);
    if (changeable.runInThread()) {
      ApplyChangableJob job = new ApplyChangableJob(changeable);
      jobsManager.enqueue(job);
    } else {
      changeable.apply();
    }
  }

  /**
   * Determines if an revert can be performed.
   * @return
   */
  public boolean canUndo(){
    return currentIndex != parentNode;
  }

  /**
   * Determines if a apply can be performed.
   * @return
   */
  public boolean canRedo(){
    return currentIndex.right != null;
  }

  /**
   * Undoes the Changeable at the current index.
   * @throws IllegalStateException if canUndo returns false.
   */
  public void undo(){
    //validate
    if ( !canUndo() ){
      throw new IllegalStateException("Cannot revert. Index is out of range.");
    }

    if (currentIndex.changeable.runInThread()) {
      RevertChangeableJob job = new RevertChangeableJob(currentIndex.changeable);
      jobsManager.enqueue(job);
    } else {
      currentIndex.changeable.revert();
    }

    moveLeft();
  }

  /**
   * Moves the internal pointer of the backed linked list to the left.
   * @throws IllegalStateException If the left index is null.
   */
  private void moveLeft(){
    if ( currentIndex.left == null ){
      throw new IllegalStateException("Internal index set to null.");
    }
    currentIndex = currentIndex.left;

    for (ChangeManagerListener listener : listeners) {
      listener.onChangeManagerChange(this);
    }
    System.gc();
  }

  /**
   * Moves the internal pointer of the backed linked list to the right.
   * @throws IllegalStateException If the right index is null.
   */
  private void moveRight(){
    if ( currentIndex.right == null ){
      throw new IllegalStateException("Internal index set to null.");
    }
    currentIndex = currentIndex.right;
    for (ChangeManagerListener listener : listeners) {
      listener.onChangeManagerChange(this);
    }
    System.gc();
  }

  /**
   * Redoes the Changable at the current index.
   * @throws IllegalStateException if canRedo returns false.
   */
  public void redo(){
    //validate
    if ( !canRedo() ){
      throw new IllegalStateException("Cannot apply. Index is out of range.");
    }
    //reset index
    moveRight();

    if (currentIndex.changeable.runInThread()) {
      ApplyChangableJob job = new ApplyChangableJob(currentIndex.changeable);
      jobsManager.enqueue(job);
    } else {
      currentIndex.changeable.apply();
    }
  }

  public Changeable getCurrent() {
    if (currentIndex != null) {
      return currentIndex.changeable;
    } else {
      return null;
    }
  }

  @Override
  public void dispose() {
    listeners.clear();
    clear();
    jobsManager = null;
  }

  public boolean haveChanges() {
    return canUndo() || canRedo();
  }

  private class Node {
    private Node left  = null;
    private Node right = null;

    private final Changeable changeable;

    public Node(Changeable c){
      changeable = c;
    }

    public Node(){
      changeable = null;
    }
  }
}
