var AnnotatedStringModelMeta = {
	def: {
		1: function(cin, m){ m.hello = cin.readString();},
		3: function(cin, m){ m.key = cin.readString();},
		4: function(cin, m){ m.aliased = cin.readString();},
	},
	createEmptyModel: function(){
		var m = {};
		m.hello = null;
		m.key = null;
		m.aliased = null;
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
