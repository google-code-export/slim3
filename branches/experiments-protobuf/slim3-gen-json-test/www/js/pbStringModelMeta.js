var StringModelMeta = {
	def: {
		1: function(cin, m){ m.hello = cin.readString();},
		2: function(cin, m){ m.japanese = cin.readString();},
		3: function(cin, m){ m.japanese_hiragana = cin.readString();},
		4: function(cin, m){ m.japanese_katakana = cin.readString();},
		5: function(cin, m){ m.key = cin.readString();},
		6: function(cin, m){ m.surrogate = cin.readString();},
	},
	createEmptyModel: function(){
		var m = {};
		m.hello = null;
		m.japanese = null;
		m.japanese_hiragana = null;
		m.japanese_katakana = null;
		m.key = null;
		m.surrogate = null;
		return m;
	},
	readModel: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModel(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
	readModels: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModels(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
};
