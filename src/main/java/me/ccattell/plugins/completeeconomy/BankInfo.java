package me.ccattell.plugins.completeeconomy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class BankInfo{
	
	//Hashmap that saves Player's name and their cash
	public final static HashMap<String, Integer> hashMoney = new HashMap<String, Integer>();
	
	private File file;
	private CompleteEconomy plugin;
	
	public BankInfo(CompleteEconomy p, File f){
		file = f;
		plugin = p;
		
		if(!this.file.exists()){
			try{
				this.file.createNewFile();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
		LoadFile();
	}
	
	public int getPlayerAccount(String pname){
		int i=0;
		if(CheckPlayer(pname)){
			i = hashMoney.get(pname);
			return i;
		}
		else return 0;
	}
	
	public String GetPlayer(String pname){
		String l=null;
		
		if(CheckPlayer(pname)){
			l = hashMoney.get(pname.toLowerCase()).toString();
		}
		else{
			l="Error! Your account does not exist";
		}
		return " " +l+ " "+plugin.moneyName;
	}
	
	public void SaveFile(){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(String p:hashMoney.keySet()){
				bw.write(p.toLowerCase() + "#"+hashMoney.get(p));
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch(Exception e){
			plugin.getLogger().severe("Player Information file did not save properly");
		}
	}
	
	public void LoadFile(){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String l;
			while((l=br.readLine())!=null){
				String[] args = l.split("[#]",2);
				if (args.length !=2) continue;
				String p = args[0].replaceAll(" ", "");
				p = p.toLowerCase();
				String b = args[1].replaceAll(" ", "");
				hashMoney.put(p, Integer.parseInt(b));
			}
			br.close();
		} catch (Exception e){
			plugin.getLogger().severe("Player Information file did not load properly");
		}
	}
	
	public boolean CheckPlayer(String pname){
		if(hashMoney.containsKey(pname.toLowerCase())){
			return true;
		}
		else
			return false;
	}
	
	public void NewPlayer(String p, int i){
		hashMoney.put(p.toLowerCase(), i);
		SaveFile();
		LoadFile();
	}
	
	public void DepositMoney(String p, int i){
		if(hashMoney.containsKey(p.toLowerCase())){
			int previous = hashMoney.get(p.toLowerCase());
			hashMoney.put(p.toLowerCase(),i+previous);
			if(plugin.getServer().getPlayer(p)!=null){plugin.getServer().getPlayer(p).sendMessage("You deposited "+i+" "+plugin.moneyName);}
		}
		else{
			NewPlayer(p.toLowerCase(), i);
		}
		SaveFile();
		LoadFile();
	}
	
	public void WithdrawMoney(String p, int i){
		int previous = hashMoney.get(p.toLowerCase());
		
		if(hashMoney.containsKey(p.toLowerCase())){
			if(hashMoney.get(p.toLowerCase())-i<0){
				hashMoney.put(p.toLowerCase(), 0);
			}
			else{
				hashMoney.put(p.toLowerCase(), previous-i);
				if(plugin.getServer().getPlayer(p)!=null){plugin.getServer().getPlayer(p).sendMessage("You withdrew "+i+" "+plugin.moneyName);}
			}
		}
		else{
			NewPlayer(p.toLowerCase(),0);
		}
		SaveFile();
		LoadFile();
	}
}


