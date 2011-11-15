var FloatModelMeta = {
	def: {
		1: function(cin, m){ m.infinityValue = cin.readFloat();},
		2: function(cin, m){ m.key = cin.readString();},
		3: function(cin, m){ m.maxValue = cin.readFloat();},
		4: function(cin, m){ m.minNormalValue = cin.readFloat();},
		5: function(cin, m){ m.minP1Value = cin.readFloat();},
		6: function(cin, m){ m.minValue = cin.readFloat();},
		7: function(cin, m){ m.nanValue = cin.readFloat();},
		8: function(cin, m){ m.negativeFloatValue = cin.readFloat();},
		9: function(cin, m){ m.negativeInfinityValue = cin.readFloat();},
		10: function(cin, m){ m.positive = cin.readFloat();},
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
	readModel: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModel(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
	readModels: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModels(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
};
