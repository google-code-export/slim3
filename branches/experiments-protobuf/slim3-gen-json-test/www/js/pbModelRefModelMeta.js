var ModelRefModelMeta = {
	def: {
		1: function(cin, m){ m.intValue = cin.readInt32();},
		2: function(cin, m){ m.key = cin.readString();},
		3: function(cin, m, maxDepth, curDepth){
			if(curDepth == maxDepth){
				m.modelRefValue = cin.readString();
			} else{
				var size = cin.readRawVarint32();
				cin.pushLimit(size);
				m.modelRefValue = ModelRefModelMeta.readModel(cin, maxDepth, curDepth + 1);
				cin.popLimit();
			}
		},
	},
	createEmptyModel: function(){
		var m = {};
		m.intValue = null;
		m.key = null;
		m.modelRefValue = null;
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
