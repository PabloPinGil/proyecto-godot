extends CharacterBody2D

var wheel_base = 50
var steering_angle = 20
var steer_angle
var engine_power = 800
var acceleration = Vector2.ZERO
var friction = -0.9
var drag = -0.0015
var braking = -450
var max_speed_reverse = 6000
var slip_speed = 800
var traction_fast = 0.1
var traction_slow = 0.7

@export var target : NodePath  # Referencia al jugador

func _ready():
	# Asegúrate de que el enemigo tenga un objetivo (el jugador).
	if target == NodePath(""):
		print("Error: No se ha asignado un objetivo para el enemigo.")

func _physics_process(delta):
	acceleration = Vector2.ZERO
	chase_target()
	apply_friction()
	calculate_steering(delta)
	velocity += acceleration * delta
	move_and_slide()

func chase_target():
	# Obtenemos la posición del jugador
	if target == NodePath("") or not get_node_or_null(target):
		return  # Si no hay objetivo, no hacemos nada
	var player = get_node(target)
	var direction_to_player = (player.global_position - global_position).normalized()
	var distance_to_player = global_position.distance_to(player.global_position)

	# Control básico para perseguir al jugador
	if distance_to_player > wheel_base:
		acceleration = transform.x * engine_power
	else:
		acceleration = Vector2.ZERO

	# Ajusta el ángulo del volante hacia el jugador
	var angle_to_player = direction_to_player.angle()
	var current_angle = transform.x.angle()
	var angle_difference = wrapf(angle_to_player - current_angle, -PI, PI)
	steer_angle = clamp(angle_difference, -deg_to_rad(steering_angle), deg_to_rad(steering_angle))

func apply_friction():
	if velocity.length() < 5:
		velocity = Vector2.ZERO

	var friction_force = velocity * friction
	var drag_force = velocity * velocity.length() * drag

	if velocity.length() < 100:
		friction_force *= 3

	acceleration += drag_force + friction_force

func calculate_steering(delta):
	var rear_wheel = position - transform.x * wheel_base / 2.0
	var front_wheel = position + transform.x * wheel_base / 2.0

	rear_wheel += velocity * delta
	front_wheel += velocity.rotated(steer_angle) * delta

	var new_heading = (front_wheel - rear_wheel).normalized()

	var traction = traction_slow
	if velocity.length() > slip_speed:
		traction = traction_fast

	var d = new_heading.dot(velocity.normalized())
	if d > 0:
		velocity = velocity.lerp(new_heading * velocity.length(), traction)
	if d < 0:
		velocity = -new_heading * min(velocity.length(), max_speed_reverse)
	rotation = new_heading.angle()
