var LongModelMeta = {
	def: {
		1: function(cin, v){ v.almostMin1 = cin.readInt64();},
		2: function(cin, v){ v.almostMin2 = cin.readInt64();},
		3: function(cin, v){ v.key = cin.readString();},
		4: function(cin, v){ v.max = cin.readInt64();},
		5: function(cin, v){ v.min = cin.readInt64();},
		6: function(cin, v){ v.minus1 = cin.readInt64();},
		7: function(cin, v){ v.minusB20 = cin.readInt64();},
		8: function(cin, v){ v.minusB36 = cin.readInt64();},
		9: function(cin, v){ v.negative = cin.readInt64();},
		10: function(cin, v){ v.positive = cin.readInt64();},
		11: function(cin, v){ v.v2p52 = cin.readInt64();},
		12: function(cin, v){ v.v2p53 = cin.readInt64();},
		13: function(cin, v){ v.v2p54 = cin.readInt64();},
	},
	createEmptyModel: function(){
		var m = {};
		m.almostMin1 = null;
		m.almostMin2 = null;
		m.key = null;
		m.max = null;
		m.min = null;
		m.minus1 = null;
		m.minusB20 = null;
		m.minusB36 = null;
		m.negative = null;
		m.positive = null;
		m.v2p52 = null;
		m.v2p53 = null;
		m.v2p54 = null;
		return m;
	},
	readModel: function(input){
		return pbCommon.readModel(input, this.def, this.createEmptyModel);
	},
	readModels: function(input){
		return pbCommon.readModels(input, this.def, this.createEmptyModel);
	},
};
