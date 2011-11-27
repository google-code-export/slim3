function appendTitle(name){
	document.getElementById("results").innerHTML += makeTitle(name);
}
function makeTitle(name){
	return "<tr>" +
		"<th colspan=\"4\">" + name + "</th></tr>" +
		"<tr><th>status</th><th>name</th><th>expected</th><th>actual</th></tr>";
}

function appendResult(name, expected, actual){
	document.getElementById("results").innerHTML += makeResult(name, expected, actual);
}
function makeResult(name, expected, actual){
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
	return "<tr>" +
		"<td bgcolor=\"" + (equals ? "#00ff00\">OK!" : "#ff0000\">NG!") + "</td>" +
		"<td>" + name + "</td>" +
		"<td>" + expected + "</td>" +
		"<td>" + actual + "</td></tr>\n";
}

function appendNestedResult(name, expected, actual){
	var buff = "";
	for(f in expected){
		buff += makeResult(name + "." + f, expected[f], actual[f + ""]);
	}
	document.getElementById("results").innerHTML += buff;
}

function appendCollectionResult(name, expected, actual){
//	makeResult(name, expected, actual);
}

function appendModelArrayResult(name, expected, actual){
	var buff = "";
	for(i = 0; i < expected.length; i++){
		for(f in expected[i]){
			var v = null;
			if(actual != null && actual[i] != null){
				v = actual[i][f + ""];
			}
			buff += makeResult(name + "[" + i + "]." + f, expected[i][f], v);
		}
	}
	document.getElementById("results").innerHTML += buff;
}

function longToHexString(v){
	var s = (((v / Math.pow(2, 32)) & 0xffffffff) >>> 0).toString(16);
	var str = "00000000".slice(s.length, 8) + s;
	s = ((v & 0xffffffff) >>> 0).toString(16);
	str += "00000000".slice(s.length, 8) + s;
	return "0x" + str;
}

tests = new Array();
