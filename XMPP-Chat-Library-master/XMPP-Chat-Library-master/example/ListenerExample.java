import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.listeners.ChatListener;
import com.github.theholywaffle.lolchatapi.listeners.FriendListener;
import com.github.theholywaffle.lolchatapi.riotapi.RateLimit;
import com.github.theholywaffle.lolchatapi.riotapi.RiotApiKey;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;

public class ListenerExample {

	public static void main(String[] args) {
		new ListenerExample();
	}

	public ListenerExample() {
		final LolChat api = new LolChat(ChatServer.EUW,
				FriendRequestPolicy.ACCEPT_ALL, new RiotApiKey("RIOT-API-KEY",
						RateLimit.DEFAULT));
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
		api.addChatListener(new ChatListener() {

			public void onMessage(Friend friend, String message) {
				System.out.println("[MSG]>" + friend.getName() + ": " + message);

			}
		});

//		// Example 3: Adding ChatListener to 1 Friend only - this only
//		// listens to messages from this friend only
//		api.getFriendByName("Dyrus").setChatListener(new ChatListener() {
//
//			public void onMessage(Friend friend, String message) {
//				// Dyrus sent us a message
//				System.out.println("Dyrus: " + message);
//			}
//		});

		if (api.login("myusername", "mypassword")) {
			System.out.println("Initialized Successfully...");
			// ...
		}
	}

	public static void queryUser(String user){
		Friend userf = api.getFriendByName(user);
		LolStatus status = userf.getStatus();
		System.out.println("Current divison: " + status.getRankedLeagueDivision());
		System.out.println("Current GameStatus: " + status.getGameStatus());
		System.out.println("Spectating: " + status.getSpectatedGameId());
		System.out.println("Normal Leaves: " + status.getNormalLeaves());
		status.
	}

}
