package me.khrystal.library;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/23
 * update time:
 * email: 723526676@qq.com
 */
public class ItemAction {

    private String title;
    private int actionId = -1;
    private boolean selected;
    private boolean sticky;

    public ItemAction(int actionId, String title) {
        this.title = title;
        this.actionId = actionId;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitle() {
        return this.title;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public int getActionId() {
        return actionId;
    }

    /**
     * @param sticky  true for sticky, pop up sends event but does not disappear
     */
    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    /**
     * @return  true if button is sticky, menu stays visible after press
     */
    public boolean isSticky() {
        return sticky;
    }

    /**
     * Set selected flag;
     *
     * @param selected Flag to indicate the item is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Check if item is selected
     *
     * @return true or false
     */
    public boolean isSelected() {
        return this.selected;
    }
}
