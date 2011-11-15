var IntModelMeta = {
	def: {
		1: function(cin, m){ m.key = cin.readString();},
		2: function(cin, m){ m.max = cin.readInt32();},
		3: function(cin, m){ m.min = cin.readInt32();},
		4: function(cin, m){ m.negative = cin.readInt32();},
		5: function(cin, m){ m.positive = cin.readInt32();},
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
	readModel: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModel(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
	readModels: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModels(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
};
