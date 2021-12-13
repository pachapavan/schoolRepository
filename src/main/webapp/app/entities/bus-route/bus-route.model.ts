import { IBusRouteName } from 'app/entities/bus-route-name/bus-route-name.model';
import { IBusStops } from 'app/entities/bus-stops/bus-stops.model';
import { IStudent } from 'app/entities/student/student.model';

export interface IBusRoute {
  id?: number;
  routeName?: string | null;
  routeDriver?: string | null;
  busNumber?: number | null;
  year?: number | null;
  status?: string | null;
  comments?: string | null;
  busRoutes?: IBusRouteName[] | null;
  busStops?: IBusStops[] | null;
  student?: IStudent | null;
}

export class BusRoute implements IBusRoute {
  constructor(
    public id?: number,
    public routeName?: string | null,
    public routeDriver?: string | null,
    public busNumber?: number | null,
    public year?: number | null,
    public status?: string | null,
    public comments?: string | null,
    public busRoutes?: IBusRouteName[] | null,
    public busStops?: IBusStops[] | null,
    public student?: IStudent | null
  ) {}
}

export function getBusRouteIdentifier(busRoute: IBusRoute): number | undefined {
  return busRoute.id;
}
