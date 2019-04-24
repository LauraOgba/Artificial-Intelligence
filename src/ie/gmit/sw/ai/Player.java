package ie.gmit.sw.ai;

public class Player{
	
	private double playerHealth = 100;
	private double sword = 0;
	private double angerLevel = 0;
	private int keepHealth = 0;
	private int bomb = 0;
	private int hBomb = 0;
	
	public Player() {
	}
	
	
	public double getSword() {
		return sword;
	}


	public void setSword(double sword) {
		this.sword = sword;
		// Picking up a sword
		if(sword == 1){
			keepHealth = 3;
		}
	}

	public double getAngerLevel() {
		return angerLevel;
	}
	
	public void addAnger(){
		if (this.angerLevel >= 95){
			this.angerLevel = 100;
		}else{
			this.angerLevel += 5;
		}
	}

	public void setAngerLevel(double angerLevel) {
		this.angerLevel = angerLevel;
	}


	public int getKeepHealth() {
		return keepHealth;
	}


	public void setKeepHealth(int keepHealth) {
		this.keepHealth = keepHealth;
	}


	public int getBomb() {
		return bomb;
	}


	public void setBomb(int bomb) {
		this.bomb = bomb;
	}


	public int gethBomb() {
		return hBomb;
	}


	public void sethBomb(int hBomb) {
		this.hBomb = hBomb;
	}


	public double getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(double playerHealth) {
		if (playerHealth <= 0){
			System.out.println("You were killed at: " + (System.currentTimeMillis() - GameRunner.time) + "ms"); 
			//SoundEffects.DEATH.play();
			System.exit(0);
		}
		this.playerHealth = playerHealth;
	}


	public double fight() {
		// Calculate damage to spider
			double extradamage = this.angerLevel / 10.0;
			extradamage = Math.round(extradamage);
			
			if(sword == 1){
				keepHealth--;
				if(keepHealth == 0){
					// Sword broke
					sword = 0;
				}
				return 10 + extradamage;
			}else{
				return 5 + extradamage;
			}
		}


}