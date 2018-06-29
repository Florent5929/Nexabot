package fr.florent.bot.commands;

import fr.florent.bot.BotDiscord;
import fr.florent.bot.commands.Command.ExecutorType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class Commands {

	private BotDiscord botdiscord;

	public Commands(BotDiscord botdiscord) {
		this.botdiscord = botdiscord;
	}

	@Command(name = "vote", description = "Permet de voter pour un ou plusieurs candidats.", type = ExecutorType.USER)
	public void onVote(User user, String[] args) {

		if (args.length < 1) {
			this.sendPrivateMessage(user, user.getAsMention() + " Vous devez renseigner au moins un candidat.");
		} else {
			MessageChannel voteChannel = botdiscord.getJda().getTextChannelsByName("vote", true).get(0);

			if (voteChannel != null) {
				voteChannel
						.sendMessage(
								user.getName() + " approuve le(s) candidat(s) suivant(s) : " + this.toStringArgs(args) + ".")
						.queue();
				this.sendPrivateMessage(user, user.getAsMention() + " Votre vote a bien été enregistré.");
			} else {
				this.sendPrivateMessage(user, user.getAsMention()
						+ " Il n'y a pas de channel vote sur ce serveur, vous ne pouvez pas voter.");
			}
		}

	}

	public String toStringArgs(String[] args) {

		String result = new String("");

		for (String arg : args) {
			result = new String(result + arg + ", ");
		}
		if (result.length()-2 >= 1) {
			result = result.substring(0, result.length() - 2);
			return result;
		} else {
			return "";
		}

	}

	@Command(name = "help", description = "Montre les commandes du bot.", type = ExecutorType.ALL)
	public void onHelp(User user, MessageChannel channel) {
		String output = new String(user.getAsMention() + " Les commandes disponibles sont : \n");

		for (SimpleCommand cmd : botdiscord.getCommandMap().getCommands()) {
			output = new String(output + "!" + cmd.getName() + " : " + cmd.getDescription() + "\n");
		}

		this.sendPrivateMessage(user, output);
	}

	@Command(name = "info", description = "Donne votre nom et votre channel.", type = ExecutorType.ALL)
	public void onInfo(User user, MessageChannel channel) {
		channel.sendMessage(
				user.getAsMention() + " a tapé la commande !info dans le channel " + channel.getName() + ".").queue();
	}

	@Command(name = "stop", description = "Arrête le bot.", type = ExecutorType.CONSOLE)
	public void onStop() {
		botdiscord.setRunning(false);
	}

	public void sendPrivateMessage(User user, String content) {
		// openPrivateChannel provides a RestAction<PrivateChannel>
		// which means it supplies you with the resulting channel
		user.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(content).queue();
		});
	}

}
