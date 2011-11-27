var WrappersListModelMeta = {
	def: {
		1: function(cin, m, maxDepth, curDepth){
			if(m.booleanList == null) m.booleanList = new Array();
			m.booleanList.push(cin.readBool());
		},
		2: function(cin, m, maxDepth, curDepth){
			if(m.doubleList == null) m.doubleList = new Array();
			m.doubleList.push(cin.readDouble());
		},
		3: function(cin, m, maxDepth, curDepth){
			if(m.floatList == null) m.floatList = new Array();
			m.floatList.push(cin.readFloat());
		},
		4: function(cin, m, maxDepth, curDepth){
			if(m.integerList == null) m.integerList = new Array();
			m.integerList.push(cin.readInt32());
		},
		5: function(cin, m){ m.key = cin.readString();},
		6: function(cin, m, maxDepth, curDepth){
			if(m.longList == null) m.longList = new Array();
			m.longList.push(cin.readInt64());
		},
		7: function(cin, m, maxDepth, curDepth){
			if(m.shortList == null) m.shortList = new Array();
			m.shortList.push(cin.readInt32());
		},
	},
	createEmptyModel: function(){
		var m = {};
		m.booleanList = null;
		m.doubleList = null;
		m.floatList = null;
		m.integerList = null;
		m.key = null;
		m.longList = null;
		m.shortList = null;
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
