package game;

import java.util.ArrayList;

/**
 * Playable Player object.
 *
 * @author Pedro Caetano
 * @version 2.0
 */
public class Player extends Character {
    private final Log log;
    private final ArrayList<Item> inventory = new ArrayList<>();
    private final ArrayList<Wearable> wearables = new ArrayList<>();
    private Profile currentProfile;
    private int currentSteps = -1;
    private boolean playing = true;

    /**
     * Initialises a new Player.
     *
     * @param profile     Profile that is playing the current game
     * @param gameManager GameManager that will be managing player
     */
    public Player(Profile profile, GameManager gameManager) {
        log = new Log(this.getClass().getSimpleName());
        setProfile(profile);
        setGameManager(gameManager);
    }

    /**
     * Removes a Wearable from the player Inventory.
     *
     * @param index Index of Wearable to be removed from Wearables
     */
    void removeWearable(int index) {
        wearables.remove(index);
    }

    /**
     * Gets an ArrayList of Items the player is carrying.
     *
     * @return ArrayList of items Player is carrying
     */
    ArrayList<Item> getInventory() {
        return this.inventory;
    }

    /**
     * Gets an ArrayList of Wearables the player is carrying.
     *
     * @return ArrayList of Wearables Player is carrying
     */
    ArrayList<Wearable> getWearables() {
        return this.wearables;
    }

    /**
     * Adds an Item to the Inventory.
     *
     * @param newItem Item to be added to Player Inventory
     */
    void addItem(Item newItem) {
        inventory.add(newItem);
    }

    /**
     * Removes an Item from the Inventory.
     *
     * @param item Item tem to be removed from Inventory
     */
    void removeItem(Item item) {
        inventory.remove(item);
    }

    /**
     * Adds a Wearable to the player Wearables.
     *
     * @param newWearable Wearable to be added
     */
    void addWearable(Wearable newWearable) {
        if (!wearables.contains(newWearable)) {
            wearables.add(newWearable);
        }
    }

    /**
     * Allows the killing of the player.
     */
    void kill() {
        log.add("Player has died and removed from the map", 2);
        if (this.currentCell != null) {
            this.currentCell.setCharacter(null);
        }
        setCurrentCell(null);
        setSteps(-1);
        GameSounds sound = new GameSounds();
        sound.playDeath();
    }

    /**
     * Checks whether the Player has an associated cell and is therefor alive.
     *
     * @return True if Player is alive
     */
    boolean isAlive() {
        return currentCell != null;
    }

    /**
     * Gets the current profile associated with the Player.
     *
     * @return Profile playing the game
     */
    Profile getCurrentProfile() {
        return this.currentProfile;
    }

    /**
     * Gets current total step count.
     *
     * @return Current step count
     */
    int getCurrentSteps() {
        return this.currentSteps;
    }

    /**
     * Sets the step count of the Player.
     *
     * @param steps Steps to set
     */
    void setSteps(int steps) {
        this.currentSteps = steps;
    }

    /**
     * Performs the necessary actions after winning the game, such as playing sound and resetting currentCell.
     */
    void win() {
        if (this.currentCell != null) {
            this.currentCell.setCharacter(null);
        }
        this.playing = false;
        GameSounds sound = new GameSounds();
        sound.playWin();
    }

    /**
     * Whether the Player is still playing the Game.
     *
     * @return True if Player is playing
     */
    boolean isPlaying() {
        return playing;
    }

    /**
     * Counts number of Tokens in the current Inventory.
     *
     * @return Number of Tokens
     */
    int getTokenCount() {
        int tokenCount = 0;
        for (Item i : getInventory()) {
            if (i instanceof Token) {
                tokenCount += 1;
            }
        }
        return tokenCount;
    }


    /**
     * Set the current user Profile playing the game.
     *
     * @param profile Profile that is playing the current game
     */
    private void setProfile(Profile profile) {
        this.currentProfile = profile;
    }

    /**
     * Sets the current GameManager in charge of the player.
     *
     * @param gameManager Current GameManager instance
     */
    private void setGameManager(GameManager gameManager) {
    }

    /**
     * Method check if player have Sword
     *
     * @return true if player wearables contain sword false otherwise
     */
    public boolean checkIfContainSword(){
        boolean doesS = false;
        for(Wearable w : wearables){
            if (w instanceof Sword){
                doesS = true;
            }
        }
        return doesS;
    }

    /**
     * Method to delete sword form player wearables list
     *
     */
    public void deteletSword(){
        for(int i = 0; i < wearables.size(); i++){
            if (wearables.get(i) instanceof Sword){
                wearables.remove(i);
            }
        }
    }

    /**
     * Method check if player have Pickaxe
     *
     * @return true if player wearables contain pickaxe false otherwise
     */
    public boolean checkIfcontainPicaxe(){
        boolean doesP = false;
        for (Wearable w : wearables){
            if (w instanceof Pickaxe){
                doesP = true;
            }
        }
        return doesP;
    }

}
