public interface Creature extends Entity {
   //enum Species();
    int getHealth();

    /**
     * @param damage how much damage the creature is given
     * @return how much damage the creature actually took
     */
    int receiveDamage(int damage);
    void receiveHealth(int health);

    int getAttack();
    int getDefense();
    int getMoveSpeed();
    float getCritDam();
    float getCritRate();


}
