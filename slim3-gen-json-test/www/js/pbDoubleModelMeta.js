var DoubleModelMeta = {
	def: {
		1: function(cin, v){ v.doubleValue = cin.readDouble();},
		2: function(cin, v){ v.infinityValue = cin.readDouble();},
		3: function(cin, v){ v.key = cin.readString();},
		4: function(cin, v){ v.maxValue = cin.readDouble();},
		5: function(cin, v){ v.minM2Value = cin.readDouble();},
		6: function(cin, v){ v.minNormalValue = cin.readDouble();},
		7: function(cin, v){ v.minValue = cin.readDouble();},
		8: function(cin, v){ v.nanValue = cin.readDouble();},
		9: function(cin, v){ v.negativeDoubleValue = cin.readDouble();},
		10: function(cin, v){ v.negativeInfinityValue = cin.readDouble();},
	},
	createEmptyModel: function(){
		var m = {};
		m.doubleValue = null;
		m.infinityValue = null;
		m.key = null;
		m.maxValue = null;
		m.minM2Value = null;
		m.minNormalValue = null;
		m.minValue = null;
		m.nanValue = null;
		m.negativeDoubleValue = null;
		m.negativeInfinityValue = null;
		return m;
	},
	readModel: function(input){
		return pbCommon.readModel(input, this.def, this.createEmptyModel);
	},
	readModels: function(input){
		return pbCommon.readModels(input, this.def, this.createEmptyModel);
	},
};
