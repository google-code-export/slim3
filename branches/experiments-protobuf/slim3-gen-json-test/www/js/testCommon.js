function appendTitle(name){
	document.getElementById("results").innerHTML += "<tr>" +
		"<th colspan=\"4\">" + name + "</th></tr>" +
		"<tr><th>status</th><th>name</th><th>expected</th><th>actual</th></tr>";
}
function appendResult(name, expected, actual){
	var equals = expected == actual;
	if(expected instanceof Array && actual instanceof Array){
		var len = expected.length;
		if(len == actual.length){
			for(var i = 0; i < len; i++){
				equals = expected[i] == actual[i];
				if(!equals) break;
			}
		}
	} else if(typeof(expected) == "number" && typeof(actual) == "number" &&
			isNaN(expected) && isNaN(actual)){
		equals = true;
	}
	document.getElementById("results").innerHTML += "<tr>" +
		"<td bgcolor=\"" + (equals ? "#00ff00\">OK!" : "#ff0000\">NG!") + "</td>" +
		"<td>" + name + "</td>" +
		"<td>" + expected + "</td>" +
		"<td>" + actual + "</td></tr>";
}
tests = new Array();
