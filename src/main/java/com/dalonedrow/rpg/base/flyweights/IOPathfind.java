package com.dalonedrow.rpg.base.flyweights;

public final class IOPathfind {
    /**
     * Adds a flag.
     * @param flag the flag
     */
    public void addFlag(final long flag) {
        flags |= flag;
    }
    public void clearFlags() {
        flags = 0;
    }
    /**
     * Removes a flag.
     * @param flag the flag
     */
    public void removeFlag(final long flag) {
        flags &= ~flag;
    }
    /**
     * Determines if the {@link BaseInteractiveObject} has a specific flag.
     * @param flag the flag
     * @return true if the {@link BaseInteractiveObject} has the flag; false
     *         otherwise
     */
    public boolean hasFlag(final long flag) {
        return (flags & flag) == flag;
    }
    private long flags;
    private int listnb; 
    private int[] list;  
    private int listPosition;
    private int pathwait;
    private int    truetarget;
    public int getListItem(final int index) {
        return list[index];
    }
    public boolean hasList() {
        return list != null;
    }
    /**
     * @return the listnb
     */
    public int getListnb() {
        return listnb;
    }
    /**
     * @param listnb the listnb to set
     */
    public void setListnb(int listnb) {
        this.listnb = listnb;
    }
    /**
     * @return the listPosition
     */
    public int getListPosition() {
        return listPosition;
    }
    /**
     * @param listPosition the listPosition to set
     */
    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }
    /**
     * @return the pathwait
     */
    public int getPathwait() {
        return pathwait;
    }
    /**
     * @param pathwait the pathwait to set
     */
    public void setPathwait(int pathwait) {
        this.pathwait = pathwait;
    }
    /**
     * @return the truetarget
     */
    public int getTruetarget() {
        return truetarget;
    }
    /**
     * @param truetarget the truetarget to set
     */
    public void setTruetarget(int truetarget) {
        this.truetarget = truetarget;
    }
}
