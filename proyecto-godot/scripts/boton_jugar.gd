extends Button


func _physics_process(delta: float) -> void:
	if is_pressed():
		get_tree().change_scene_to_file("res://scenes/escenario_parque_bebe.tscn")
		
