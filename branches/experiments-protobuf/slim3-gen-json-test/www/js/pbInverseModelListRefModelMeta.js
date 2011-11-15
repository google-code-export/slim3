var InverseModelListRefModelMeta = {
	def: {
		1: function(cin, m, maxDepth, curDepth){
			if(m.children == null) m.children = new Array();
			if(curDepth == maxDepth){
				m.children.push(cin.readString());
			} else{
				var size = cin.readRawVarint32();
				cin.pushLimit(size);
				m.children.push(InverseModelListRefModelMeta.readModel(cin, maxDepth, curDepth + 1));
				cin.popLimit();
			}
		},
		2: function(cin, m){ m.intValue = cin.readInt32();},
		3: function(cin, m){ m.key = cin.readString();},
		4: function(cin, m, maxDepth, curDepth){
			if(curDepth == maxDepth){
				m.parent = cin.readString();
			} else{
				var size = cin.readRawVarint32();
				cin.pushLimit(size);
				m.parent = InverseModelListRefModelMeta.readModel(cin, maxDepth, curDepth + 1);
				cin.popLimit();
			}
		},
	},
	createEmptyModel: function(){
		var m = {};
		m.children = null;
		m.intValue = null;
		m.key = null;
		m.parent = null;
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
