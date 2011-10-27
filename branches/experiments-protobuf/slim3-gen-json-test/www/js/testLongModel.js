tests.push({
	path: "data/LongModel.bin",
	onload: function(){
		var model = LongModelMeta.readModel(this.responseText);
		appendTitle("LongModel");
		appendResult("max", "9223372036854775807", model.max);
		appendResult("max_hi32(hex)", "0x7fffffff00000000", longToHexString(model.max_hi32));
		appendResult("max_low32(hex)", "0x00000000ffffffff", longToHexString(model.max_low32));
		appendResult("positive", "32342423444", model.positive);
		appendResult("positive_hi32(hex)", "0x0000000700000000", longToHexString(model.positive_hi32));
		appendResult("positive_low32(hex)", "0x0000000087c23794", longToHexString(model.positive_low32));
		appendResult("negative", "-23948821142", model.negative);
		appendResult("negative_hi32(hex)", "0xfffffffa00000000", longToHexString(model.negative_hi32));
		appendResult("negative_low32(hex)", "0x000000006c89fd6a", longToHexString(model.negative_low32));
		appendResult("min", "-9223372036854775808", model.min);
		appendResult("min_hi32(hex)", "0x8000000000000000", longToHexString(model.min_hi32));
		appendResult("min_low32(hex)", "0x0000000000000000", longToHexString(model.min_low32));
		appendResult("minus1", "-1", model.minus1);
		appendResult("minus1_hi32(hex)", "0xffffffff00000000", longToHexString(model.minus1_hi32));
		appendResult("minus1_low32(hex)", "0x00000000ffffffff", longToHexString(model.minus1_low32));
		appendResult("v2p52", "4503599627370496", model.v2p52);
		appendResult("v2p52_hi32(hex)", "0x0010000000000000", longToHexString(model.v2p52_hi32));
		appendResult("v2p52_low32(hex)", "0x0000000000000000", longToHexString(model.v2p52_low32));
		appendResult("v2p53", "9007199254740992", model.v2p53);
		appendResult("v2p53_hi32(hex)", "0x0020000000000000", longToHexString(model.v2p53_hi32));
		appendResult("v2p53_low32(hex)", "0x0000000000000000", longToHexString(model.v2p53_low32));
		appendResult("v2p54", "18014398509481984", model.v2p54);
		appendResult("v2p54_hi32(hex)", "0x0040000000000000", longToHexString(model.v2p54_hi32));
		appendResult("v2p54_low32(hex)", "0x0000000000000000", longToHexString(model.v2p54_low32));
		appendResult("minusB20", "-1035162", model.minusB20);
		appendResult("minusB20_hi32(hex)", "0xffffffff00000000", longToHexString(model.minusB20_hi32));
		appendResult("minusB20_low32(hex)", "0x00000000fff03466", longToHexString(model.minusB20_low32));
		appendResult("minusB36", "-7794613171", model.minusB36);
		appendResult("minusB36_hi32(hex)", "0xfffffffe00000000", longToHexString(model.minusB36_hi32));
		appendResult("minusB36_low32(hex)", "0x000000002f67a44d", longToHexString(model.minusB36_low32));
		appendResult("almostMin1", "-4096", model.almostMin1);
		appendResult("almostMin1_hi32(hex)", "0xffffffff00000000", longToHexString(model.almostMin1_hi32));
		appendResult("almostMin1_low32(hex)", "0x00000000fffff000", longToHexString(model.almostMin1_low32));
		appendResult("almostMin2", "-9221120237041090561", model.almostMin2);
		appendResult("almostMin2_hi32(hex)", "0x8007ffff00000000", longToHexString(model.almostMin2_hi32));
		appendResult("almostMin2_low32(hex)", "0x00000000ffffffff", longToHexString(model.almostMin2_low32));
	}
});
