var ModelRefModelMeta = {
	def: {
		1: function(cin, v){ v.intValue = cin.readInt32();},
		2: function(cin, v){ v.key = cin.readString();},
		3: function(cin, v){
			var size = cin.readRawVarint32();
			cin.pushLimit(size);
			v.modelRefValue = ModelRefModelMeta.readModel(cin);
			cin.popLimit();
		},
	},
	createEmptyModel: function(){
		var m = {};
		m.intValue = null;
		m.key = null;
		m.modelRefValue = null;
		return m;
	},
	readModel: function(input){
		return pbCommon.readModel(input, this.def, this.createEmptyModel);
	},
	readModels: function(input){
		return pbCommon.readModels(input, this.def, this.createEmptyModel);
	},
};
