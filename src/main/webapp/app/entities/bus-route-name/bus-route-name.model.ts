import { IBusRoute } from 'app/entities/bus-route/bus-route.model';

export interface IBusRouteName {
  id?: number;
  routeName?: string | null;
  busRoute?: IBusRoute | null;
}

export class BusRouteName implements IBusRouteName {
  constructor(public id?: number, public routeName?: string | null, public busRoute?: IBusRoute | null) {}
}

export function getBusRouteNameIdentifier(busRouteName: IBusRouteName): number | undefined {
  return busRouteName.id;
}
