var DoubleModelMeta = {
	def: {
		1: function(cin, v){ v.doubleValue = cin.readDouble();},
		2: function(cin, v){ v.key = cin.readString();},
	},
	readModel: function(text){
		return pbCommon.readModel(text, this.def);
	},
	readModels: function(text){
		return pbCommon.readModels(text, this.def);
	},
};
