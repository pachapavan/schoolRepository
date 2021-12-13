import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusRoute, getBusRouteIdentifier } from '../bus-route.model';

export type EntityResponseType = HttpResponse<IBusRoute>;
export type EntityArrayResponseType = HttpResponse<IBusRoute[]>;

@Injectable({ providedIn: 'root' })
export class BusRouteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bus-routes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(busRoute: IBusRoute): Observable<EntityResponseType> {
    return this.http.post<IBusRoute>(this.resourceUrl, busRoute, { observe: 'response' });
  }

  update(busRoute: IBusRoute): Observable<EntityResponseType> {
    return this.http.put<IBusRoute>(`${this.resourceUrl}/${getBusRouteIdentifier(busRoute) as number}`, busRoute, { observe: 'response' });
  }

  partialUpdate(busRoute: IBusRoute): Observable<EntityResponseType> {
    return this.http.patch<IBusRoute>(`${this.resourceUrl}/${getBusRouteIdentifier(busRoute) as number}`, busRoute, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusRoute>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusRoute[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBusRouteToCollectionIfMissing(busRouteCollection: IBusRoute[], ...busRoutesToCheck: (IBusRoute | null | undefined)[]): IBusRoute[] {
    const busRoutes: IBusRoute[] = busRoutesToCheck.filter(isPresent);
    if (busRoutes.length > 0) {
      const busRouteCollectionIdentifiers = busRouteCollection.map(busRouteItem => getBusRouteIdentifier(busRouteItem)!);
      const busRoutesToAdd = busRoutes.filter(busRouteItem => {
        const busRouteIdentifier = getBusRouteIdentifier(busRouteItem);
        if (busRouteIdentifier == null || busRouteCollectionIdentifiers.includes(busRouteIdentifier)) {
          return false;
        }
        busRouteCollectionIdentifiers.push(busRouteIdentifier);
        return true;
      });
      return [...busRoutesToAdd, ...busRouteCollection];
    }
    return busRouteCollection;
  }
}
