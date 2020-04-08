// For event listener
var usernameForm = document.querySelector("#usernameForm");
var messageForm = document.querySelector("#messageForm");

// Hide - unhide
var usernameInput = document.querySelector(".container-login");
var chatArea = document.querySelector(".container-chat-area");
var connectingElement = document.querySelector('.connecting');



// Message area

var chat = document.querySelector(".container-chat");
var activeUsers = document.querySelector("#active-users-list")



function connect (event) {

    // Get the name from the user.
    username = document.querySelector("#name").value.trim();

    // Switch from login to chat
    usernameInput.style.display = "none";
    chatArea.style.display = "flex";

    // Create the connection with websocket
   var socket = new SockJS('http://localhost:8600/websocket'); // ZUUL doesn't support Websocket :(
//     var socket = new SockJS('http://localhost:8762/chat/websocket');
    stompClient = Stomp.over(socket);


    // Connect to the websocket.
    stompClient.connect({} , onConnected , onError)

    // Prevent the page from reloading, (we bypass the default form action)
    event.preventDefault();

}

// Successful connection
function onConnected (){




    // Subscribe to "/topic/greetings" for new messages
    stompClient.subscribe('/topic/chat-stream', onMessageReceived)
//    var url = "http://localhost:8400/chatlog"; // Monolithic

    // Send the username to the server, to add you to the Active Users list.
    loginContent = {username : username}
    stompClient.send("/app/login" , {} , JSON.stringify(loginContent))
    

    // Get Past messages
    var url = "http://localhost:8762/api/chatlog";
    axios.get(url)
        .then(function(response){
            onPastLogsReceived(response);
            
            console.log(response);
        })
        .catch(function(error){
            console.log(error);
        })
    var activeUsersUrl = "http://localhost:8762/api/activeUsers";
    axios.get(activeUsersUrl)
        .then(function (response){
            onActiveUsersReceived(response);
        })
        .catch(function(error){
            console.log(error)
        })



}

// Something went wrong
function onError (error) {
    console.log(error);
    connectingElement.textContent = error;
    connectingElement.style.color = "red";
}

// Handling when the user sends the messages
function sendMessage (event) {

    // Get the message content
    var message = document.querySelector("#message").value.trim();

    // Bundle up the info of author , and message text
    messageContent = {authorName : username, message : message }
    document.querySelector("#message").value = ""
    stompClient.send("/app/chat" , {} , JSON.stringify(messageContent))

    console.log("We are sending" , messageContent);

// Prevent the page from reloading, (we bypass the default form action)
    event.preventDefault();
}

function onPastLogsReceived(payload){
    payload.data.reverse().forEach(element => {

        authorName = element.authorName;
        message = element.message;
        timeSent = element.timeSent.slice(11 , 16);
        toAdd = authorName + " : " +  message + " -- " + timeSent;
        // Create a list item
        var messageElement = document.createElement("li");
        // messageElement.classList.add("list-group-item")
        if (authorName === username){
            messageElement.classList.add("own-message")
        }
        else {
            messageElement.classList.add("other-message")
        }
        var messageText = document.createTextNode(toAdd);
        messageElement.appendChild(messageText);
        chat.appendChild(messageElement);
    });


}

function onActiveUsersReceived(payload){
    payload.data.forEach(element => {

        activeUser = element.username;
        // We are skiping ourselves, since we handle this through messageListener
        if (activeUser == username){
            return;
        }
        var messageElement = document.createElement("li");
        messageElement.classList.add("list-group-item");

        var messageText = document.createTextNode(activeUser);
        messageElement.appendChild(messageText);
        activeUsers.appendChild(messageElement);
    })


}

// Handling when something is posted on the subscribed channel
function onMessageReceived(payload){

    message = JSON.parse(payload.body);



    console.log("WE START HERE THE MESSAGE IS : " , message.type);

    if (message.type === "JOIN"){
        console.log("I AM A JOIN MESSAGE");
        activeUser = message.authorName

        var messageElement = document.createElement("li");
        messageElement.classList.add("list-group-item");

        var messageText = document.createTextNode(activeUser);

        messageElement.appendChild(messageText);

        activeUsers.appendChild(messageElement);

    }
    else if (message.type === "LEAVE"){
        console.log("I AM A LEAVE MESSAGE");

        leaver = message.authorName;
        for (var i =0; i < activeUsers.children.length; i++){


            if (leaver ==activeUsers.children.item(i).firstChild.data ){
                activeUsers.removeChild(activeUsers.childNodes.item(i+1));
            }

            // try {
            //     console.log(activeUsers.children.item(i).firstChild.data)
            // }

        }
            

    }
    else {

        console.log("Nothing to see here please disperse");



    }

    time = message.timeSent.slice(11 , 16)
    toAdd = message.authorName + " : " +  message.message + " -- " + time;

        // Create a list item
    var messageElement = document.createElement("li");
    // messageElement.classList.add("list-group-item")
    if (message.authorName === username){
        messageElement.classList.add("own-message")
    }
    else {
        messageElement.classList.add("other-message")
    }
    var messageText = document.createTextNode(toAdd);

    messageElement.appendChild(messageText);

    chat.appendChild(messageElement);

}


usernameForm.addEventListener("submit" , connect , true)
messageForm.addEventListener("submit" , sendMessage , true)



/*

Timeline of javascript :

1) get the referrence with the querySelector of the login form.
2) add the event listened to the submition of the username.
3) when the user enters the username and submits, we trigger the connect function
Connect function get's the name , connects to the websocket.

var test = document.querySelector("#messageArea");
for (let item of test.children) { console.log(item.firstChild.data)}

*/

