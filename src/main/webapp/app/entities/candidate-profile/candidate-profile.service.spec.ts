import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { CandidateProfile } from '@/shared/model/candidate-profile.model';

import CandidateProfileService from './candidate-profile.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('CandidateProfile Service', () => {
    let service: CandidateProfileService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CandidateProfileService();
      currentDate = new Date();
      elemDefault = new CandidateProfile(
        123,
        currentDate,
        'AAAAAAA',
        'HIGH',
        'HIGH',
        'HIGH',
        'HIGH',
        'HIGH',
        'HIGH',
        'CORE_CANDIDATE',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { candidateDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a CandidateProfile', async () => {
        const returnedFromService = { id: 123, candidateDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { candidateDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a CandidateProfile', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a CandidateProfile', async () => {
        const returnedFromService = {
          candidateDate: dayjs(currentDate).format(DATE_FORMAT),
          cultivateDirection: 'BBBBBB',
          stability: 'BBBBBB',
          learningAbility: 'BBBBBB',
          communicationCoordination: 'BBBBBB',
          businessUnderstanding: 'BBBBBB',
          responsibility: 'BBBBBB',
          riskAwareness: 'BBBBBB',
          judgement: 'BBBBBB',
          evidence: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { candidateDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a CandidateProfile', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a CandidateProfile', async () => {
        const patchObject = { stability: 'BBBBBB', learningAbility: 'BBBBBB', businessUnderstanding: 'BBBBBB', ...new CandidateProfile() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { candidateDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a CandidateProfile', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of CandidateProfile', async () => {
        const returnedFromService = {
          candidateDate: dayjs(currentDate).format(DATE_FORMAT),
          cultivateDirection: 'BBBBBB',
          stability: 'BBBBBB',
          learningAbility: 'BBBBBB',
          communicationCoordination: 'BBBBBB',
          businessUnderstanding: 'BBBBBB',
          responsibility: 'BBBBBB',
          riskAwareness: 'BBBBBB',
          judgement: 'BBBBBB',
          evidence: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { candidateDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of CandidateProfile', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a CandidateProfile', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a CandidateProfile', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
