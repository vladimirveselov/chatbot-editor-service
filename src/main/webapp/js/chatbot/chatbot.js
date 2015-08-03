var query = new Query();

function Chatbot() {
	this.response = "";
}

function Query() {
	this.text = "";
	this.session_id = null;
}

var answer = function(question) {
	console.log(question);
	var xhr = new XMLHttpRequest();
	query.response = "не понял";
	query.text = question;
	var json = JSON.stringify(query);
	xhr.open('post', 'webapi/talk/query', true);
	xhr.setRequestHeader('Accept', 'application/json');
	xhr.setRequestHeader("Content-type", "application/json");
	xhr.setRequestHeader("Content-Length", json.length);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			query = JSON.parse(xhr.response);
			showResponse();
		}
	}
	xhr.send(json);
}

var showResponse = function() {
	if (query != null) {
		chatbot.response = query.response;
		var divv = document.getElementById('answer');
		divv.innerHTML = chatbot.response;
	}
}

var chatbot = new Chatbot();

function ask(event, text) {
	if (event.keyCode == 13) {
		answer(text.value);
	}
}
