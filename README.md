# lol-utility

A utility application that allows you to interact with the League of Legends Chat. You will need to put in your riot api key in the code in order to get full functionality.
Program can be run by going to 'out/artifacts/lol_utility_jar', and typing in 'java -jar lol-utility.jar'
Note: With the introduction of the new league client, many of the custom chat settings such as 'setStatus, setStatusOptions' will no longer work.

The application will automatically display friend list status changes and incoming messages
Other Functions available:
getOnlineFriends - gets your online friends
sendMessage - send a message to a particular player x number of times
reply - sends a message to the person that last sent you a message. Will return 'N/A' if nobody has sent you a message
sendToLastSent - sends a message to the person that you last sent a message to. Will return 'N/A' if you have not sent a previous message yet
autoReply - Automatically replies to anybody that sends you a message with a default message
queryUser - Get summoner information for the particular user
getState - get the current state of your chat
setStatus - sets your status to available/away/offline
setStatusOptions - sets your status to predefined values in the code
quit - quits the application


