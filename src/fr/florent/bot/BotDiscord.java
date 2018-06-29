package fr.florent.bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import fr.florent.bot.commands.CommandMap;
import fr.florent.bot.events.BotListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class BotDiscord implements Runnable {
	
	private String TOKEN;
	private final JDA jda;
	private final CommandMap commandmap = new CommandMap(this);
	private boolean running;
	private final Scanner scanner = new Scanner(System.in);
	
	public BotDiscord() throws LoginException, InterruptedException, IOException{
		TOKEN = this.getToken();
		jda = new JDABuilder(AccountType.BOT).setToken(TOKEN).setBulkDeleteSplittingEnabled(false).buildBlocking();
		getJda().addEventListener(new BotListener(commandmap));
		System.out.println("Nexabot est connecté !");
	}

	public static void main(String[] args) {

		try {
			BotDiscord botdiscord = new BotDiscord();
			new Thread(botdiscord, "bot").start();
		} catch (LoginException | InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

	public String getToken() throws IOException{
		
		File file = getFile("token");
		
		if(file.length() == 0){
			FileWriter fw = new FileWriter("token.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("key:YOUR_TOKEN_KEY");
			bw.close();
			return "YOUR_TOKEN_KEY";
		}
		
		FileReader fr = new FileReader("token.txt");
		BufferedReader br = new BufferedReader(fr);
		String ligne;
		if((ligne = br.readLine()) != null){
			String[] split = ligne.split(":");
			br.close();
			return split[1];	
		} else {
			br.close();
			return "YOUR_TOKEN_KEY";
		}
	}
	
	
	@Override
	public void run() {
		running = true;
		
		while(running){
			if(scanner.hasNextLine()){
				commandmap.commandConsole(scanner.nextLine());
			}
		}
		
		scanner.close();
		System.out.println("Bot stopped.");
		getJda().shutdown();
		System.exit(0);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public JDA getJda() {	
		return jda;
	}
	
	public CommandMap getCommandMap(){
		return commandmap;
	}
	
	public static File getFile(String name) {
		name = name + ".txt";
		File file = new File(name);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
				System.out.println("Could not create file " + name);
			}
		}
		return file;
	}

}
