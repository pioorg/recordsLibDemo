/*
 *
 *  Copyright (C) 2021 Piotr Przybył
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package dev.softwaregarden.recordsLibDemo;

import dev.softwaregarden.recordsLibDemo.dto.Request;
import dev.softwaregarden.recordsLibDemo.dto.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Endpoint {

    /*
    curl -X POST --location "http://localhost:8080/check" -H "Content-Type: application/json" -d '{"one":"uppercase", "two":12 }'
     */
    @PostMapping(path = "check")
    public Response post(@RequestBody Request request) {
        return new Response(request.one().toUpperCase(),
            request.two() * request.two());
    }
}
