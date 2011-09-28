var DoubleModelMeta = {
	def: {
		1: function(cin, v){ v.doubleValue = cin.readDouble();},
		2: function(cin, v){ v.infinityValue = cin.readDouble();},
		3: function(cin, v){ v.key = cin.readString();},
		4: function(cin, v){ v.nanValue = cin.readDouble();},
		5: function(cin, v){ v.negativeDoubleValue = cin.readDouble();},
		6: function(cin, v){ v.negativeInfinityValue = cin.readDouble();},
	},
	readModel: function(text){
		return pbCommon.readModel(text, this.def);
	},
	readModels: function(text){
		return pbCommon.readModels(text, this.def);
	},
};
