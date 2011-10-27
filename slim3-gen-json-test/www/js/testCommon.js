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
function longToHexString(v){
	var s = (((v / Math.pow(2, 32)) & 0xffffffff) >>> 0).toString(16);
	var str = "00000000".slice(s.length, 8) + s;
	s = ((v & 0xffffffff) >>> 0).toString(16);
	str += "00000000".slice(s.length, 8) + s;
	return "0x" + str;
}
tests = new Array();
