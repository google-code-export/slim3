var FloatModelMeta = {
	def: {
		1: function(cin, v){ v.infinityValue = cin.readFloat();},
		2: function(cin, v){ v.key = cin.readString();},
		3: function(cin, v){ v.maxValue = cin.readFloat();},
		4: function(cin, v){ v.minNormalValue = cin.readFloat();},
		5: function(cin, v){ v.minP1Value = cin.readFloat();},
		6: function(cin, v){ v.minValue = cin.readFloat();},
		7: function(cin, v){ v.nanValue = cin.readFloat();},
		8: function(cin, v){ v.negativeFloatValue = cin.readFloat();},
		9: function(cin, v){ v.negativeInfinityValue = cin.readFloat();},
		10: function(cin, v){ v.positive = cin.readFloat();},
	},
	createEmptyModel: function(){
		var m = {};
		m.infinityValue = null;
		m.key = null;
		m.maxValue = null;
		m.minNormalValue = null;
		m.minP1Value = null;
		m.minValue = null;
		m.nanValue = null;
		m.negativeFloatValue = null;
		m.negativeInfinityValue = null;
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
