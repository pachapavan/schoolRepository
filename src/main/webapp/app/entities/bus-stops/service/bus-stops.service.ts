import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusStops, getBusStopsIdentifier } from '../bus-stops.model';

export type EntityResponseType = HttpResponse<IBusStops>;
export type EntityArrayResponseType = HttpResponse<IBusStops[]>;

@Injectable({ providedIn: 'root' })
export class BusStopsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bus-stops');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(busStops: IBusStops): Observable<EntityResponseType> {
    return this.http.post<IBusStops>(this.resourceUrl, busStops, { observe: 'response' });
  }

  update(busStops: IBusStops): Observable<EntityResponseType> {
    return this.http.put<IBusStops>(`${this.resourceUrl}/${getBusStopsIdentifier(busStops) as number}`, busStops, { observe: 'response' });
  }

  partialUpdate(busStops: IBusStops): Observable<EntityResponseType> {
    return this.http.patch<IBusStops>(`${this.resourceUrl}/${getBusStopsIdentifier(busStops) as number}`, busStops, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusStops>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusStops[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBusStopsToCollectionIfMissing(busStopsCollection: IBusStops[], ...busStopsToCheck: (IBusStops | null | undefined)[]): IBusStops[] {
    const busStops: IBusStops[] = busStopsToCheck.filter(isPresent);
    if (busStops.length > 0) {
      const busStopsCollectionIdentifiers = busStopsCollection.map(busStopsItem => getBusStopsIdentifier(busStopsItem)!);
      const busStopsToAdd = busStops.filter(busStopsItem => {
        const busStopsIdentifier = getBusStopsIdentifier(busStopsItem);
        if (busStopsIdentifier == null || busStopsCollectionIdentifiers.includes(busStopsIdentifier)) {
          return false;
        }
        busStopsCollectionIdentifiers.push(busStopsIdentifier);
        return true;
      });
      return [...busStopsToAdd, ...busStopsCollection];
    }
    return busStopsCollection;
  }
}
