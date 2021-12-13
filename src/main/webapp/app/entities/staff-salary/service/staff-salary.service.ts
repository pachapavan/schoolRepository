import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStaffSalary, getStaffSalaryIdentifier } from '../staff-salary.model';

export type EntityResponseType = HttpResponse<IStaffSalary>;
export type EntityArrayResponseType = HttpResponse<IStaffSalary[]>;

@Injectable({ providedIn: 'root' })
export class StaffSalaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/staff-salaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(staffSalary: IStaffSalary): Observable<EntityResponseType> {
    return this.http.post<IStaffSalary>(this.resourceUrl, staffSalary, { observe: 'response' });
  }

  update(staffSalary: IStaffSalary): Observable<EntityResponseType> {
    return this.http.put<IStaffSalary>(`${this.resourceUrl}/${getStaffSalaryIdentifier(staffSalary) as number}`, staffSalary, {
      observe: 'response',
    });
  }

  partialUpdate(staffSalary: IStaffSalary): Observable<EntityResponseType> {
    return this.http.patch<IStaffSalary>(`${this.resourceUrl}/${getStaffSalaryIdentifier(staffSalary) as number}`, staffSalary, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStaffSalary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStaffSalary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStaffSalaryToCollectionIfMissing(
    staffSalaryCollection: IStaffSalary[],
    ...staffSalariesToCheck: (IStaffSalary | null | undefined)[]
  ): IStaffSalary[] {
    const staffSalaries: IStaffSalary[] = staffSalariesToCheck.filter(isPresent);
    if (staffSalaries.length > 0) {
      const staffSalaryCollectionIdentifiers = staffSalaryCollection.map(staffSalaryItem => getStaffSalaryIdentifier(staffSalaryItem)!);
      const staffSalariesToAdd = staffSalaries.filter(staffSalaryItem => {
        const staffSalaryIdentifier = getStaffSalaryIdentifier(staffSalaryItem);
        if (staffSalaryIdentifier == null || staffSalaryCollectionIdentifiers.includes(staffSalaryIdentifier)) {
          return false;
        }
        staffSalaryCollectionIdentifiers.push(staffSalaryIdentifier);
        return true;
      });
      return [...staffSalariesToAdd, ...staffSalaryCollection];
    }
    return staffSalaryCollection;
  }
}
