import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusRouteName, getBusRouteNameIdentifier } from '../bus-route-name.model';

export type EntityResponseType = HttpResponse<IBusRouteName>;
export type EntityArrayResponseType = HttpResponse<IBusRouteName[]>;

@Injectable({ providedIn: 'root' })
export class BusRouteNameService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bus-route-names');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(busRouteName: IBusRouteName): Observable<EntityResponseType> {
    return this.http.post<IBusRouteName>(this.resourceUrl, busRouteName, { observe: 'response' });
  }

  update(busRouteName: IBusRouteName): Observable<EntityResponseType> {
    return this.http.put<IBusRouteName>(`${this.resourceUrl}/${getBusRouteNameIdentifier(busRouteName) as number}`, busRouteName, {
      observe: 'response',
    });
  }

  partialUpdate(busRouteName: IBusRouteName): Observable<EntityResponseType> {
    return this.http.patch<IBusRouteName>(`${this.resourceUrl}/${getBusRouteNameIdentifier(busRouteName) as number}`, busRouteName, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusRouteName>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusRouteName[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBusRouteNameToCollectionIfMissing(
    busRouteNameCollection: IBusRouteName[],
    ...busRouteNamesToCheck: (IBusRouteName | null | undefined)[]
  ): IBusRouteName[] {
    const busRouteNames: IBusRouteName[] = busRouteNamesToCheck.filter(isPresent);
    if (busRouteNames.length > 0) {
      const busRouteNameCollectionIdentifiers = busRouteNameCollection.map(
        busRouteNameItem => getBusRouteNameIdentifier(busRouteNameItem)!
      );
      const busRouteNamesToAdd = busRouteNames.filter(busRouteNameItem => {
        const busRouteNameIdentifier = getBusRouteNameIdentifier(busRouteNameItem);
        if (busRouteNameIdentifier == null || busRouteNameCollectionIdentifiers.includes(busRouteNameIdentifier)) {
          return false;
        }
        busRouteNameCollectionIdentifiers.push(busRouteNameIdentifier);
        return true;
      });
      return [...busRouteNamesToAdd, ...busRouteNameCollection];
    }
    return busRouteNameCollection;
  }
}
