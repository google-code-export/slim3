var IntModelMeta = {
	def: {
		1: function(cin, v){ v.key = cin.readString();},
		2: function(cin, v){ v.max = cin.readInt32();},
		3: function(cin, v){ v.min = cin.readInt32();},
		4: function(cin, v){ v.negative = cin.readInt32();},
		5: function(cin, v){ v.positive = cin.readInt32();},
	},
	createEmptyModel: function(){
		var m = {};
		m.key = null;
		m.max = null;
		m.min = null;
		m.negative = null;
		m.positive = null;
		return m;
	},
	readModel: function(text){
		return pbCommon.readModel(text, this.def, this.createEmptyModel);
	},
	readModels: function(text){
		return pbCommon.readModels(text, this.def, this.createEmptyModel);
	},
};
