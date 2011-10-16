var StringModelMeta = {
	def: {
		1: function(cin, v){ v.hello = cin.readString();},
		2: function(cin, v){ v.japanese = cin.readString();},
		3: function(cin, v){ v.japanese_hiragana = cin.readString();},
		4: function(cin, v){ v.japanese_katakana = cin.readString();},
		5: function(cin, v){ v.key = cin.readString();},
		6: function(cin, v){ v.surrogate = cin.readString();},
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
	readModel: function(input){
		return pbCommon.readModel(input, this.def, this.createEmptyModel);
	},
	readModels: function(input){
		return pbCommon.readModels(input, this.def, this.createEmptyModel);
	},
};
