tests.push({
	path: "data/StringModel.bin",
	onload: function(){
		var model = StringModelMeta.readModel(this.responseText);
		appendTitle("StringModel");
		appendResult("hello", "Hello, World.", model.hello);
		appendResult("surrogate", "surro-𝄢-gate", model.surrogate);
		appendResult("japanese", "お早うございます。行ってらっしゃい。", model.japanese);
		appendResult("japanese_hiragana", "あいうえおかきくけこわおん", model.japanese_hiragana);
		appendResult("japanese_katakana", "アイウエオカキクケコヤユヨ", model.japanese_katakana);
	}
});
