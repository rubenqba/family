Cómo ejecutar
===

# Creando familias 

```jshelllanguage
echo 'Family 1' | http POST http://localhost:8080/family
echo 'Family 2' | http POST http://localhost:8080/family
```

# Creando personas
```jshelllanguage
grandpa=$(echo '{"first_name": "Enrique", "last_name": "Camps Cruz", "birth_day" : -1507766602, "sex" : 0 }' | http --json POST http://localhost:8080/people | jq .id)
grandma=$(echo '{"first_name": "Rosa Aida", "last_name": "García Piñirí", "birth_day" : -1057607822, "sex" : 1 }' | http --json POST http://localhost:8080/people | jq .id)
mom=$(echo '{"first_name": "Trinidad", "last_name": "Camps Garcia", "birth_day" : 1507766602, "sex" : 1 }' | http --json POST http://localhost:8080/people | jq .id)
aunt=$(echo '{"first_name": "Rosa", "last_name": "Camps Garcia", "birth_day" : 1507766602, "sex" : 1 }' | http --json POST http://localhost:8080/people | jq .id)
uncle=$(echo '{"first_name": "Enrique", "last_name": "Camps Garcia", "birth_day" : 1507766602, "sex" : 0 }' | http --json POST http://localhost:8080/people | jq .id)
dad=$(echo '{"first_name": "Rubén", "last_name": "Bresler González", "birth_day" : 1507766602, "sex" : 0 }' | http --json POST http://localhost:8080/people | jq .id)
me=$(echo '{"first_name": "Rubén", "last_name": "Bresler Camps", "birth_day" : 1507766602, "sex" : 0 }' | http --json POST http://localhost:8080/people | jq .id)
brother=$(echo '{"first_name": "Roylán", "last_name": "Bresler Camps", "birth_day" : 1507766602, "sex" : 0 }' | http --json POST http://localhost:8080/people | jq .id)
sister1=$(echo '{"first_name": "Rosanny", "last_name": "Bresler Camps", "birth_day" : 1507766602, "sex" : 1 }' | http --json POST http://localhost:8080/people | jq .id)
mother_in_law=$(echo '{"first_name": "Deysi", "last_name": "Frometa Matos", "birth_day" : 1507766602, "sex" : 1 }' | http --json POST http://localhost:8080/people | jq .id)
sister2=$(echo '{"first_name": "Solanch", "last_name": "Bresler Frometa", "birth_day" : 1507766602, "sex" : 1 }' | http --json POST http://localhost:8080/people | jq .id)
```

# Creando relaciones
```jshelllanguage
http POST http://localhost:8080/people/$grandpa/rel/MARRIED/$grandma | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$grandpa/rel/PARENT/$mom | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$grandpa/rel/PARENT/$aunt | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$grandpa/rel/PARENT/$uncle | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$grandma/rel/PARENT/$mom | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$grandma/rel/PARENT/$aunt | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$grandma/rel/PARENT/$uncle | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"

http POST http://localhost:8080/people/$mom/rel/SIBLING/$aunt | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$mom/rel/SIBLING/$uncle | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$aunt/rel/SIBLING/$uncle | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"

http POST http://localhost:8080/people/$mom/rel/PARENT/$me | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$mom/rel/PARENT/$brother | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$mom/rel/PARENT/$sister1 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$dad/rel/PARENT/$me | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$dad/rel/PARENT/$brother | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$dad/rel/PARENT/$sister1 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"

http POST http://localhost:8080/people/$dad/rel/MARRIED/$mother_in_law | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/2/rel/{}"
http POST http://localhost:8080/people/$dad/rel/PARENT/$sister2 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/2/rel/{}"
http POST http://localhost:8080/people/$mother_in_law/rel/PARENT/$sister2 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/2/rel/{}"

http POST http://localhost:8080/people/$me/rel/SIBLING/$brother | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$me/rel/SIBLING/$sister1 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$me/rel/SIBLING/$sister2 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/2/rel/{}"
http POST http://localhost:8080/people/$brother/rel/SIBLING/$sister1 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/1/rel/{}"
http POST http://localhost:8080/people/$brother/rel/SIBLING/$sister2 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/2/rel/{}"
http POST http://localhost:8080/people/$sister1/rel/SIBLING/$sister2 | jq '.[].id' | xargs -I '{}' http "http://localhost:8080/family/2/rel/{}"
```
