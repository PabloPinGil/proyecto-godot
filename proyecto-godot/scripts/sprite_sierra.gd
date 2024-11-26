extends Sprite2D

var rotation_speed = 400  # Velocidad de rotación en grados por segundo

func _process(delta):
	# Incrementar la rotación en grados por frame
	rotation_degrees += rotation_speed * delta
