import { IBusRouteName } from 'app/shared/model/bus-route-name.model';
import { IBusStops } from 'app/shared/model/bus-stops.model';
import { IStudent } from 'app/shared/model/student.model';

export interface IBusRoute {
  id?: number;
  routeName?: string;
  routeDriver?: string;
  busNumber?: number;
  year?: number;
  status?: string;
  comments?: string;
  busRoutes?: IBusRouteName[];
  busStops?: IBusStops[];
  student?: IStudent;
}

export class BusRoute implements IBusRoute {
  constructor(
    public id?: number,
    public routeName?: string,
    public routeDriver?: string,
    public busNumber?: number,
    public year?: number,
    public status?: string,
    public comments?: string,
    public busRoutes?: IBusRouteName[],
    public busStops?: IBusStops[],
    public student?: IStudent
  ) {}
}
