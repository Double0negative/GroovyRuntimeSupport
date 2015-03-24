package org.mcsg.groovy

import org.bukkit.Bukkit
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class GroovyRuntimeSupport extends JavaPlugin implements Listener{

	GroovyShell shell;


	void onEnable(){
		println "Groovy Runtime Support has been loaded"

		Bukkit.getPluginManager().registerEvents(this,this)

		shell = new GroovyShell()

	}


	Map<String, List<String>> commands = [:]

	@EventHandler
	public void playerTalk(AsyncPlayerChatEvent e){
		def msg = e.getMessage()
		def player = e.getPlayer()

		if(player.hasPermission("groovy.script")) {
			if(msg.startsWith("!~")) {

				msg = msg.substring(2)

				if(msg.length() > 0){
					def num = msg.toInteger()

					if(num > 0)
						 commands[player]?.remove(num)
					else
						commands[player]?.remove(commands[player].size() - num)


				} else {
					commands[player]  = []
				}
				e.setCancelled(true)

			}
			else if(msg.startsWith("!!")){
				msg = msg.substring(2)

				def com = commands[player] ?: []

				try{
					def str = com.join("\n")
					
					com.eachWithIndex { m, i ->
						player.sendMessage("${ChatColor.GRAY} $i] $m")
					}
					
					str = "$str \n $msg"
					
					player.sendMessage("${ChatColor.GRAY} *] $msg")
					
					def result = shell.evaluate(str)
					player.sendMessage(result.toString())
				} catch (ex){
					player.sendMessage(ex.toString())
				}
				e.setCancelled(true)

			}
			else if(msg.startsWith("!")) {
				msg = msg.substring(1)

				def com = commands[player] ?: []
				com.add(msg)
				commands[player] = com

				e.setCancelled(true)

			}


		}



	}
}
