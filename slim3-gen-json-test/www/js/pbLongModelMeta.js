var LongModelMeta = {
	def: {
		1: function(cin, m){
			var v = cin.readInt64();
			m.almostMin1 = v.value;
			m.almostMin1_hi32 = v.hi32;
			m.almostMin1_low32 = v.low32;
		},
		2: function(cin, m){
			var v = cin.readInt64();
			m.almostMin2 = v.value;
			m.almostMin2_hi32 = v.hi32;
			m.almostMin2_low32 = v.low32;
		},
		3: function(cin, m){ m.key = cin.readString();},
		4: function(cin, m){
			var v = cin.readInt64();
			m.max = v.value;
			m.max_hi32 = v.hi32;
			m.max_low32 = v.low32;
		},
		5: function(cin, m){
			var v = cin.readInt64();
			m.min = v.value;
			m.min_hi32 = v.hi32;
			m.min_low32 = v.low32;
		},
		6: function(cin, m){
			var v = cin.readInt64();
			m.minus1 = v.value;
			m.minus1_hi32 = v.hi32;
			m.minus1_low32 = v.low32;
		},
		7: function(cin, m){
			var v = cin.readInt64();
			m.minusB20 = v.value;
			m.minusB20_hi32 = v.hi32;
			m.minusB20_low32 = v.low32;
		},
		8: function(cin, m){
			var v = cin.readInt64();
			m.minusB36 = v.value;
			m.minusB36_hi32 = v.hi32;
			m.minusB36_low32 = v.low32;
		},
		9: function(cin, m){
			var v = cin.readInt64();
			m.negative = v.value;
			m.negative_hi32 = v.hi32;
			m.negative_low32 = v.low32;
		},
		10: function(cin, m){
			var v = cin.readInt64();
			m.positive = v.value;
			m.positive_hi32 = v.hi32;
			m.positive_low32 = v.low32;
		},
		11: function(cin, m){
			var v = cin.readInt64();
			m.v2p52 = v.value;
			m.v2p52_hi32 = v.hi32;
			m.v2p52_low32 = v.low32;
		},
		12: function(cin, m){
			var v = cin.readInt64();
			m.v2p53 = v.value;
			m.v2p53_hi32 = v.hi32;
			m.v2p53_low32 = v.low32;
		},
		13: function(cin, m){
			var v = cin.readInt64();
			m.v2p54 = v.value;
			m.v2p54_hi32 = v.hi32;
			m.v2p54_low32 = v.low32;
		},
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
	readModel: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModel(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
	readModels: function(input, maxDepth, curDepth){
		if(typeof(maxDepth) == "undefined") maxDepth = 1;
		return pbCommon.readModels(input, this.def, this.createEmptyModel, maxDepth, curDepth);
	},
};
