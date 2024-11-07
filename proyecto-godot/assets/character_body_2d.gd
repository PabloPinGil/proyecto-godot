extends CharacterBody2D


var wheel_base = 50  # distancia entre ruedas
var steering_angle = 20  # ángulo de giro máximo de las ruedas delanteras
var steer_angle  # ángulo de las ruedas
var engine_power = 800  # fuerza de acelerazión
var acceleration = Vector2.ZERO
var friction = -0.9  # fuerza de fricción
var drag = -0.0015  # fuerza de arrastre
var braking = -450  # fuerza de freno
var max_speed_reverse = 350  #velocidad máxima en marcha atrás
var slip_speed = 800  # velocidad donde se reduce la tracción
var traction_fast = 0.1  # tracción a alta velocidad
var traction_slow = 0.7  # tracción a baja velocidad



func _physics_process(delta):
	acceleration = Vector2.ZERO
	get_input()
	apply_friction()
	calculate_steering(delta)
	velocity += acceleration * delta 
	move_and_slide()
	print(velocity.length())


func get_input():
	var turn = 0
	if Input.is_action_pressed("steer_right"):
		turn += 1
	if Input.is_action_pressed("steer_left"):
		turn -= 1
	steer_angle = turn * deg_to_rad(steering_angle)
	if Input.is_action_pressed("accelerate"):
		acceleration = transform.x * engine_power
	if Input.is_action_pressed("brake"):
		acceleration = transform.x * braking


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
