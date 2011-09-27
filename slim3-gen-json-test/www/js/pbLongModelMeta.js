var LongModelMeta = {
	def: {
		1: function(cin, v){ v.key = cin.readString();},
		2: function(cin, v){ v.longValue = cin.readInt64();},
	},
	readModel: function(text){
		return pbCommon.readModel(text, this.def);
	},
	readModels: function(text){
		return pbCommon.readModels(text, this.def);
	},
};
