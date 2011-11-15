var DoubleModelMeta = {
	def: {
		1: function(cin, m){ m.doubleValue = cin.readDouble();},
		2: function(cin, m){ m.infinityValue = cin.readDouble();},
		3: function(cin, m){ m.key = cin.readString();},
		4: function(cin, m){ m.maxValue = cin.readDouble();},
		5: function(cin, m){ m.minM2Value = cin.readDouble();},
		6: function(cin, m){ m.minNormalValue = cin.readDouble();},
		7: function(cin, m){ m.minValue = cin.readDouble();},
		8: function(cin, m){ m.nanValue = cin.readDouble();},
		9: function(cin, m){ m.negativeDoubleValue = cin.readDouble();},
		10: function(cin, m){ m.negativeInfinityValue = cin.readDouble();},
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
	readModel: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModel(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
	readModels: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModels(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
};
