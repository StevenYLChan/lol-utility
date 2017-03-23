import com.github.theholywaffle.lolchatapi.*;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendRequestListener;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

import java.io.Console;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.logging.LogManager;

public class Listeners {

    private static String lastSentTo = "";
    private static String lastGotReplyFrom = "";

    public static void main(String[] args) {
        //Disable presence warnings
        LogManager.getLogManager().reset();
        //Disable xmlproperty warnings
        System.err.close();

        String pwd;

        Scanner input = new Scanner(System.in);
        System.out.print("Username: ");
        String usr = input.next();
        try{
            Console console = System.console();
            pwd = String.valueOf(console.readPassword("Password: "));
        }catch(Exception ex){
            System.out.print("Password: ");
            pwd = input.next();
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        final LolChat api = new LolChat(ChatServer.NA2,
                FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY",
                RateLimit.DEFAULT));

        boolean autoReplyFlag = false;

        new Listeners(api);

        Queue<String> msgQ = new ArrayDeque<>();

        ChatListener noAutoReplyChatListener = new ChatListener() {
            public void onMessage(Friend friend, String message) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + friend.getName() + " -> [MSG]: " + message);
                lastGotReplyFrom = friend.getName();
            }
        };

        ChatListener autoReplyChatListener = new ChatListener() {
            public void onMessage(Friend friend, String message) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + friend.getName() + " -> [MSG]: " + message);
                String reMsg = "Hello " + friend.getName() + ", I am currently away and will get back to you shortly.";
                System.out.println("[" + timeStamp + "] " + "[MSG] -> " + friend.getName() + ": " + reMsg);
                friend.sendMessage(reMsg);
                lastGotReplyFrom = friend.getName();
                lastSentTo = friend.getName();
                msgQ.add("[" + timeStamp + "] " + friend.getName() + " -> [MSG]: " + message);
            }
        };

        api.addChatListener(noAutoReplyChatListener);

        int option = -1;

        if (api.login(usr, pwd)) {
            System.out.println("Initialized Successfully...");
            pwd = "";
        } else {
            System.out.println("Failed to initialize...");
            option = 0;
        }

        while (option != 0) {
            System.out.println("----------------------------");
            System.out.println("Sychan LoL Utilities [AR: " + autoReplyFlag + "]\n");
            System.out.print("Options:\n(1)getOnlineFriends\n(2)sendMessage\n(3)reply\n(4)sendToLastSent\n(5)autoReply\n(6)queryUser\n(7)getState\n(8)setStatus\n(9)setStatusOptions\n(0)quit\n");
            System.out.print("----------------------------\n> ");
            option = input.nextInt();
            switch (option) {
                case 1:
                    getFriendsList(api);
                    break;
                case 2:
                    System.out.print("Recipient: ");
                    String recipient = input.next();
                    System.out.print("Message: ");
                    String message = input.next();
                    System.out.print("Number of times: ");
                    Integer no = input.nextInt();
                    sendMessage(api, recipient, message, no);
                    break;
                case 3:
                    String lastGotReplyFromTemp = lastGotReplyFrom;
                    if(lastGotReplyFromTemp.equals("")){
                        System.out.println("N/A");
                        break;
                    }
                    System.out.print("Recipient: " + lastGotReplyFromTemp);
                    System.out.print("\nMessage: ");
                    String message1 = input.next();
                    sendToLastGotReplyFrom(api, lastGotReplyFromTemp, message1);
                    break;
                case 4:
                    String lastGotSentToTemp = lastSentTo;
                    if(lastGotSentToTemp.equals("")){
                        System.out.println("N/A");
                        break;
                    }
                    System.out.print("Recipient: " + lastGotSentToTemp);
                    System.out.print("\nMessage: ");
                    String message2 = input.next();
                    sendToLastSentTo(api, lastGotSentToTemp, message2);
                    break;
                case 5:
                    autoReplyFlag = !autoReplyFlag;
                    if (autoReplyFlag) {
                        api.addChatListener(autoReplyChatListener);
                        api.removeChatListener(noAutoReplyChatListener);
                        api.disconnect();
                        api.login(usr, pwd);
                    } else {
                        if(msgQ.size()>0){
                            System.out.println("Retrieving auto replied to messages...");
                            while(msgQ.poll()!=null){
                                System.out.println(msgQ.poll());
                            }
                        }else{
                            System.out.println("No messages auto replied to");
                        }
                        api.removeChatListener(autoReplyChatListener);
                        api.addChatListener(noAutoReplyChatListener);
                        api.disconnect();
                        api.login(usr, pwd);
                    }
                    break;
                case 6:
                    System.out.print("User: ");
                    String userToQuery = input.next();
                    queryUser(api, userToQuery);
                    break;
                case 7:
                    getState(api);
                    break;
                case 8:
                    System.out.print("offline/online/away: ");
                    String status = input.next();
                    setStatus(api, status);
                    break;
                case 9:
                    setStatusOptions(api);
                    break;
                case 0:
                    System.exit(0);

            }
        }
    }

    private static void queryUser(LolChat api, String user) {
        Friend userf = api.getFriendByName(user);
        LolStatus status = userf.getStatus();
        System.out.println("\n------------------------------------");
        System.out.println("'" + user + "' LOG REPORT\n");
        System.out.println("Timestamp: " + status.getTimestamp());
        System.out.println("Level: " + status.getLevel());
        System.out.println("Current Tier: " + status.getRankedLeagueTier());
        System.out.println("Current Division: " + status.getRankedLeagueDivision());
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
        for (final Friend f : api.getOnlineFriends()) {
            System.out.println("[" + f.getGroup() + "]: " + f.getName());
        }
    }

    private static void getState(LolChat api){
        System.out.println("Loaded: " + api.isLoaded());
        System.out.println("Connected: " + api.isConnected());
        System.out.println("Online: " + api.isOnline());
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

    private static void sendMessage(LolChat api, String username, String message, int num) {
        Friend f;
        f = api.getFriendByName(username);
        if (f != null && f.isOnline()) {
            for (int i = 0; i < num; i++) {
                f.sendMessage(message);
            }
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.println("[" + timeStamp + "] " + "[MSG] -> " + username + "(" + num + "): " + message);
            lastSentTo = username;
        }
    }

    private static void sendToLastGotReplyFrom(LolChat api, String lastGotReplyFromTemp, String message) {
        Friend f;
        f = api.getFriendByName(lastGotReplyFromTemp);
        if (f != null && f.isOnline()) {
            f.sendMessage(message);
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.println("[" + timeStamp + "] " + "[MSG] -> " + lastGotReplyFromTemp + ": " + message);
            lastSentTo = lastGotReplyFromTemp;
        }
    }

    private static void sendToLastSentTo(LolChat api, String lastSentToTemp, String message) {
        Friend f;
        f = api.getFriendByName(lastSentToTemp);
        if (f != null && f.isOnline()) {
            f.sendMessage(message);
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.println("[" + timeStamp + "] " + "[MSG] -> " + lastSentToTemp + ": " + message);
            lastSentTo = lastSentToTemp;
        }
    }

    private Listeners(LolChat api) {

        /** First add or set all listeners BEFORE logging in! */
        api.addFriendListener(new FriendListener() {

            public void onFriendAvailable(Friend friend) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + "STATUS>AVAILABLE: " + friend.getName());
            }

            public void onFriendAway(Friend friend) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + "STATUS>AWAY: " + friend.getName());
            }

            public void onFriendBusy(Friend friend) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + "STATUS>BUSY: " + friend.getName());
            }

            public void onFriendJoin(Friend friend) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + "[LOG-I] " + friend.getName());
            }

            public void onFriendLeave(Friend friend) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + "[LOG-O] " + friend.getName());
            }

            public void onFriendStatusChange(Friend friend) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + friend.getName()
                        + "STATUS \u0394]: "
                        + friend.getStatus().getGameStatus());
            }

            public void onNewFriend(Friend friend) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + "New friend: " + friend.getUserId());
            }

            public void onRemoveFriend(String userId, String name) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + "Friend removed: " + userId + " " + name);
            }
        });

        api.setFriendRequestListener(new FriendRequestListener() {
            public boolean onFriendRequest(String userId, String name) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
                System.out.println("[" + timeStamp + "] " + userId + " wants to add me. Yes or no?");
                return true;
            }
        });
    }
}
