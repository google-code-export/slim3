var PrimitivesModelMeta = {
	def: {
		1: function(cin, v){ v.booleanValue = cin.readBool();},
		2: function(cin, v){ v.doubleValue = cin.readDouble();},
		3: function(cin, v){ v.floatValue = cin.readFloat();},
		4: function(cin, v){ v.intValue = cin.readInt32();},
		5: function(cin, v){ v.key = cin.readString();},
		6: function(cin, v){ v.longValue = cin.readInt64();},
		7: function(cin, v){ v.shortValue = cin.readInt32();},
	},
	readModel: function(text){
		return pbCommon.readModel(text, this.def);
	},
	readModels: function(text){
		return pbCommon.readModels(text, this.def);
	},
};
