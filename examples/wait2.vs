using std

i = 0

async while true {
	i += 10
	println("i is now " + i)
	sleep(500)
}

wait(i + 10 == 150)
println("GONEEEEEEE")
exit()