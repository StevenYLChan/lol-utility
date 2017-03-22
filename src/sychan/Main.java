package sychan;

import com.github.theholywaffle.lolchatapi.*;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.wrapper.FriendGroup;
import com.github.theholywaffle.lolchatapi.LolStatus.Queue;
import com.github.theholywaffle.lolchatapi.LolStatus.Tier;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.io.Console;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Username: ");
        String usr = input.next(); // getting a String value
        System.out.print("Password: ");
        String pwd = input.next();
//        try {
//            Console console = System.console();
//            char[] pwd = console.readPassword();
//
//            if ((console = System.console()) != null &&
//                    (pwd = console.readPassword("[%s]", "Password:")) != null) {
//                java.util.Arrays.fill(pwd, ' ');
//            }
//        } catch (Exception ex) {
//            String pwd = input.next();
//            getFriendsList(usr, pwd);
//            pwd = " ";
//        }

        int option = -1;
        while(option!=0){
            System.out.println("----------------------------");
            System.out.println("Sychan Utilities\n");
            System.out.print("Options:\n(1)getOnlineFriends\n(2)setStatus(offline/online/away)\n(3)setStatusOptions\n(4)autoReply\n(5)sendMessage\n(0) quit\n");
            System.out.print("----------------------------\n> ");
            option = input.nextInt();
            switch (option){
                case 1:
                    getFriendsList(usr, pwd);
                    break;
                case 2:
                    System.out.print("offline/online/away");
                    String status = input.next();
                    setStatus(usr,pwd,status);
                    break;
                case 3:
                    setStatusOptions(usr, pwd);
                    break;
                case 4:
                    autoReply(usr, pwd);
                    break;
                case 5:
                    System.out.print("Recipient: ");
                    String recipient = input.next();
                    System.out.print("Message: ");
                    String message = input.next();
                    System.out.print("Number of times: ");
                    Integer no = input.nextInt();
                    sendMessage(usr,pwd,recipient,message,no);
                    break;
            }
        }


    }

    private static void getFriendsList(String usr, String pwd) {
        final LolChat api = new LolChat(ChatServer.NA2,
                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("32c167c9-956d-42ff-802a-ea075b81634e",
                RateLimit.DEFAULT));
        if (api.login(usr, pwd)) {

//            // Example 1: Print out all groups and all friends in those groups
//            final Friend dyrus = api.getFriendByName("Dyrus");
//            final LolStatus status = dyrus.getStatus();
//            System.out.println("Current divison: "
//                    + status.getRankedLeagueDivision());
//            // Status be "in queue", "championselect", "ingame", "spectating",..
//            System.out.println("Current GameStatus: " + status.getGameStatus());
//            System.out.println("Spectating: " + status.getSpectatedGameId());
//            System.out.println("Normal Leaves: " + status.getNormalLeaves());
//            // ...

            for (final Friend g : api.getOnlineFriends()) {
                System.out.println("Friend: " + g.getName()); // Print out name
                // of group
            }

            api.disconnect();

//            // Example 2: Set a custom status
//            final LolStatus newStatus = new LolStatus();
//            newStatus.setLevel(1337);
//            newStatus.setRankedLeagueQueue(Queue.RANKED_SOLO_5x5);
//            newStatus.setRankedLeagueTier(Tier.CHALLENGER);
//            newStatus.setRankedLeagueName("Fiora");
//            api.setStatus(newStatus);
//
//            // Example 3: Copy status from friend
//            final LolStatus copyStatus = api.getFriendByName("Dyrus")
//                    .getStatus();
//            copyStatus.setLevel(1337); // Modify it if you like
//            api.setStatus(copyStatus); // Put it as your own status

        }
    }

//    private static void setStatus(String usr, String pwd, String status) {
//        final LolChat api = new LolChat(ChatServer.NA2,
//                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("32c167c9-956d-42ff-802a-ea075b81634e",
//                RateLimit.DEFAULT));
//        switch (status) {
//            case "offline":
//                api.setOffline();
//                break;
//            case "online":
//                api.setOnline();
//            case "away":
//                api.setChatMode(ChatMode.AWAY);
//        }
//        api.disconnect();
//    }

    private static void setStatusOptions(String usr, String pwd) {
        final LolChat api = new LolChat(ChatServer.NA2,
                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("32c167c9-956d-42ff-802a-ea075b81634e",
                RateLimit.DEFAULT));
        if (api.login(usr, pwd)) {
            LolStatus newStatus = new LolStatus();
            newStatus.setLevel(1);
            newStatus.setRankedLeagueQueue(Queue.RANKED_SOLO_5x5);
            newStatus.setRankedLeagueTier(Tier.CHALLENGER);
            newStatus.setRankedLeagueName("Sone");
            newStatus.setGameQueueType(Queue.RANKED_TEAM_5x5);
            api.setStatus(newStatus);
        }
        api.disconnect();
    }

    private static void autoReply(String usr, String pwd) {
        final LolChat api = new LolChat(ChatServer.NA2,
                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("32c167c9-956d-42ff-802a-ea075b81634e",
                RateLimit.DEFAULT));
        if (api.login(usr, pwd)) {
            api.addChatListener(new ChatListener() {
                public void onMessage(Friend friend, String message) {
                    System.out.println("[All]>" + friend.getName() + ": " + message);
                    LolStatus fStatus = friend.getStatus();
                    String reMsg = "Hello " + fStatus.getRankedLeagueTier() + " " + fStatus.getRankedLeagueDivision() + " " + friend.getName() + ", I am currently away and will get back to you shortly.";
                    System.out.println(reMsg);
                    friend.sendMessage(reMsg);
                }
            });
        }
    }

//    private static void sendMessage(String usr, String pwd, String username, String message, int num){
//        final LolChat api = new LolChat(ChatServer.NA2,
//                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("32c167c9-956d-42ff-802a-ea075b81634e",
//                RateLimit.DEFAULT));
//        if (api.login(usr, pwd)) {
//            Friend f;
//            f = api.getFriendByName(username);
//            if(f != null && f.isOnline()) {
//                for(int i = 0; i<num; i++){
//                    f.sendMessage(message);
//                    System.out.print("MSG -> " + username + ": " + i + "\r");
//                }
//            }
//        }
//        api.disconnect();
//    }

    private static void sendMessage(String usr, String pwd, String username, String message){
        final LolChat api = new LolChat(ChatServer.NA2,
                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("32c167c9-956d-42ff-802a-ea075b81634e",
                RateLimit.DEFAULT));
        if (api.login(usr, pwd)) {
            Friend f;
            f = api.getFriendByName(username);
            if(f != null && f.isOnline()) {
                    f.sendMessage(message);
            }
        }
        api.disconnect();
    }
}



