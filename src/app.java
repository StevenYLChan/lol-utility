import com.github.theholywaffle.lolchatapi.*;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendRequestListener;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.util.Scanner;

public class ListenerExample {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Username: ");
        String usr = input.next(); // getting a String value
        System.out.print("Password: ");
        String pwd = input.next();

        final LolChat api = new LolChat(ChatServer.NA2,
                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY",
                RateLimit.DEFAULT));

        boolean autoReplyFlag = false;
        new ListenerExample(api, usr, pwd, autoReplyFlag);

        int option = -1;
        while (option != 0) {
            System.out.println("----------------------------");
            System.out.println("Sychan Utilities\n");
            System.out.print("Options:\n(1)getOnlineFriends\n(2)setStatus(offline/online/away)\n(3)setStatusOptions\n(4)autoReply\n(5)sendMessage\n(0) quit\n");
            System.out.print("----------------------------\n> ");
            option = input.nextInt();
            switch (option) {
                case 1:
                    getFriendsList(api);
                    break;
                case 2:
                    System.out.print("offline/online/away");
                    String status = input.next();
                    setStatus(api, status);
                    break;
                case 3:
                    setStatusOptions(api);
                    break;
                case 4:
                    autoReplyFlag = autoReply(autoReplyFlag);
                    //api.removeChatListener(ChatListener());
                    new ListenerExample(api, usr, pwd, autoReplyFlag);
                    break;
                case 5:
                    System.out.print("Recipient: ");
                    String recipient = input.next();
                    System.out.print("Message: ");
                    String message = input.next();
                    System.out.print("Number of times: ");
                    Integer no = input.nextInt();
                    sendMessage(api, recipient, message, no);
                    break;
            }
        }
    }

    public static void queryUser(LolChat api, String user) {
        Friend userf = api.getFriendByName(user);
        LolStatus status = userf.getStatus();
        System.out.println("\n------------------------------------");
        System.out.println("'" + user + "' LOG REPORT\n");
        System.out.println("Timestamp: " + status.getTimestamp());
        System.out.println("Level: " + status.getLevel());
        System.out.println("Current Tier: " + status.getRankedLeagueTier());
        System.out.println("Current Division: " + status.getRankedLeagueDivision());
        ;
        System.out.println("Status Message: " + status.getStatusMessage());
        System.out.println("Current GameStatus: " + status.getGameStatus());
        System.out.println("Normal Wins: " + status.getNormalWins());
        System.out.println("Normal Leaves: " + status.getNormalLeaves());
        System.out.println("Ranked Wins: " + status.getRankedWins());
        System.out.println("Ranked League Name: " + status.getRankedLeagueName());
        System.out.println("Ranked League Queue: " + status.getRankedLeagueQueue());
        System.out.println("Ranked Solo Restricted: " + status.getRankedSoloRestricted());
        System.out.println("Game Queue Type: " + status.getGameQueueType());
        System.out.println("Spectating: " + status.getSpectatedGameId());
        System.out.println("Spectate Game ID:  " + status.getSpectatedGameId());
        System.out.println("Tier: " + status.getTier());
        System.out.println("Profile Icon ID: " + status.getProfileIconId());
        System.out.println("Dominion Wins: " + status.getDominionWins());
        System.out.println("Dominion Leaves:  " + status.getDominionLeaves());
        System.out.println("Featured Game Data: " + status.getFeaturedGameData());
        System.out.println("Mobile: " + status.getMobile());
        System.out.println("Skin: " + status.getSkin());
        System.out.println("------------------------------------\n");
    }

    private static void getFriendsList(LolChat api) {
        for (final Friend g : api.getOnlineFriends()) {
            System.out.println("Friend: " + g.getName());
        }
    }

    private static void setStatus(LolChat api, String status) {
        switch (status) {
            case "offline":
                api.setOffline();
                break;
            case "online":
                api.setOnline();
            case "away":
                api.setChatMode(ChatMode.AWAY);
        }
    }

    private static void setStatusOptions(LolChat api) {
        LolStatus newStatus = new LolStatus();
        newStatus.setLevel(1);
        newStatus.setRankedLeagueQueue(LolStatus.Queue.RANKED_SOLO_5x5);
        newStatus.setRankedLeagueTier(LolStatus.Tier.CHALLENGER);
        newStatus.setRankedLeagueName("Sone");
        newStatus.setGameQueueType(LolStatus.Queue.RANKED_TEAM_5x5);
        api.setStatus(newStatus);
    }

    private static boolean autoReply(boolean autoReply) {
        return !autoReply;
    }

    private static void sendMessage(LolChat api, String username, String message, int num) {
        Friend f;
        f = api.getFriendByName(username);
        if (f != null && f.isOnline()) {
            for (int i = 0; i < num; i++) {
                f.sendMessage(message);
            }
            System.out.print("MSG -> " + username + "(" + num + "): " + message);
        }
    }

    public ListenerExample(LolChat api, String username, String password, boolean autoReply) {

        /** First add or set all listeners BEFORE logging in! */

        // Example 1: Adding FriendListeners - listens to changes in your
        // friendlist
        api.addFriendListener(new FriendListener() {

            public void onFriendAvailable(Friend friend) {
                System.out.println("STATUS]AVAILABLE: " + friend.getName());
            }

            public void onFriendAway(Friend friend) {
                System.out.println("STATUS]AWAY: " + friend.getName());
            }

            public void onFriendBusy(Friend friend) {
                System.out.println("STATUS]BUSY: " + friend.getName());
            }

            public void onFriendJoin(Friend friend) {
                System.out.println("[JOINED LOBBY] " + friend.getName());
            }

            public void onFriendLeave(Friend friend) {
                System.out.println("[LEFT LOBBY] " + friend.getName());
            }

            public void onFriendStatusChange(Friend friend) {
                System.out.println(friend.getName()
                        + "STATUS \u0394]: "
                        + friend.getStatus().getGameStatus());
            }

            public void onNewFriend(Friend friend) {
                System.out.println("New friend: " + friend.getUserId());
            }

            public void onRemoveFriend(String userId, String name) {
                System.out.println("Friend removed: " + userId + " " + name);
            }
        });

        api.setFriendRequestListener(new FriendRequestListener() {
            public boolean onFriendRequest(String userId, String name) {
                System.out.println(userId + " wants to add me. Yes or no?");
                return true;
            }
        });
        // Example 2: Adding ChatListener - listens to chat messages from
        // any of your friends.
        if (autoReply) {
            api.addChatListener(new ChatListener() {
                public void onMessage(Friend friend, String message) {
                    System.out.println("[MSG]>" + friend.getName() + ": " + message);
                    String reMsg = "Hello " + friend.getName() + ", I am currently away and will get back to you shortly.";
                    System.out.println(reMsg);
                    friend.sendMessage(reMsg);
                }
            });
        } else {
            api.addChatListener(new ChatListener() {
                public void onMessage(Friend friend, String message) {
                    System.out.println("[MSG]>" + friend.getName() + ": " + message);
                }
            });
        }
//		// Example 3: Adding ChatListener to 1 Friend only - this only
//		// listens to messages from this friend only
//		api.getFriendByName("Dyrus").setChatListener(new ChatListener() {
//
//			public void onMessage(Friend friend, String message) {
//				// Dyrus sent us a message
//				System.out.println("Dyrus: " + message);
//			}
//		});
        if (api.login(username, password)) {
            System.out.println("Initialized Successfully...");
            // ...
        }

    }


}
